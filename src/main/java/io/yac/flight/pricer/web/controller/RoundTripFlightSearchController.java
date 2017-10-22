package io.yac.flight.pricer.web.controller;

import io.yac.flight.pricer.model.*;
import io.yac.flight.pricer.qpx.QPXResponse;
import io.yac.flight.pricer.search.MultiCountrySearchHandler;
import io.yac.flight.pricer.web.resources.Airline;
import io.yac.flight.pricer.web.resources.FlightSearchCriteria;
import io.yac.flight.pricer.web.resources.RoundTripFlightSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roundTripFlightSearches")
public class RoundTripFlightSearchController {


    private static final String frontEndAppUrl = "http://localhost:4200";

    private final MultiCountrySearchHandler searchFlightService;

    @Value("#{'${google.api.enabled:false}'}")
    private boolean isCallingQpxEnabled;

    @Autowired
    public RoundTripFlightSearchController(MultiCountrySearchHandler searchFlightService) {
        this.searchFlightService = searchFlightService;
    }


    @GetMapping(value = "")
    @CrossOrigin(origins = frontEndAppUrl)
    public RoundTripFlightSearchResponse search(@RequestParam("departure") String departureAirport,
                                                @RequestParam("arrival") String arrivalAirport,
                                                @RequestParam("departureDate") String departureDateStr,
                                                @RequestParam("returnDate") String returnDateStr,
                                                @RequestParam("numberOfAdult") Integer adultCount,
                                                @RequestParam(value = "currency", defaultValue = "EUR")
                                                        String currency) {

        LocalDate departureDate = LocalDate.parse(departureDateStr, DateTimeFormatter.ISO_DATE);
        LocalDate returnDate = LocalDate.parse(returnDateStr, DateTimeFormatter.ISO_DATE);

        final SliceSearchCriteria outwardJourney =
                SliceSearchCriteria.builder().origin(departureAirport).destination(arrivalAirport)
                        .departureDate(departureDate)
                        .build();

        final SliceSearchCriteria returnJourney =
                SliceSearchCriteria.builder().origin(arrivalAirport).destination(departureAirport)
                        .departureDate(returnDate).build();


        final FlightSearchCriteria searchCriteria =
                FlightSearchCriteria.builder().addSlice(outwardJourney).addSlice(returnJourney).adultCount(adultCount)
                        .ticketingCountries(Arrays.asList("FR", "CA", "US", "CH", "NL", "GB")).currency(currency)
                        .build();

        final QPXResponse qpxResponse = queryService(searchCriteria);


        RoundTripFlightSearchResponse response = new RoundTripFlightSearchResponse();
        response.setSolutions(qpxResponse.getSolutions());
        response.setOrigin(departureAirport);
        response.setDestination(arrivalAirport);
        response.setDepartureDate(departureDateStr);
        response.setReturnDate(departureDateStr);
        response.setAirlines(qpxResponse.getCarriers().stream().map(c -> new Airline(c.getIata(), c.getName())).collect(
                Collectors.toList()));
        return response;
    }

    private QPXResponse queryService(FlightSearchCriteria searchCriteria) {
        if (isCallingQpxEnabled) {
            return searchFlightService.multiCountrySearch(searchCriteria);
        } else {


            Set<Carrier> carriers = new HashSet<>();
            Set<Airport> airports = new HashSet<>();
            Set<City> cities = new HashSet<>();

            List<Solution> solutions = new ArrayList<>();
            Solution solution = new Solution();
            solution.setPrices(Arrays.asList(Price.builder().amount(1234.0).country("US").currency("USD").build(),
                    Price.builder().amount(2902.3).country("FR").currency("EUR").build()));
            solution.setRefundable(false);
            List<Slice> slices = new ArrayList<>();
            slices.add(getSlice(searchCriteria.getSlices().get(0).getOrigin(),
                    searchCriteria.getSlices().get(0).getDestination()));

            slices.add(getSlice(searchCriteria.getSlices().get(0).getDestination(),
                    searchCriteria.getSlices().get(0).getOrigin()));

            solution.setSlices(slices);


            solutions.add(solution);
            return new QPXResponse(carriers, airports, cities, solutions);
        }
    }

    private Slice getSlice(String origin, String destination) {
        Slice slice = new Slice();

        Segment segment = new Segment();
        segment.setAirline("AF");
        segment.setConnectionDuration(0);
        segment.setMarriedSegmentGroup("0");
        segment.setCabin(Cabin.ECONOMY);
        segment.setBookingCodeAvailableSeats(10);
        segment.setDuration(120);
        segment.setFlightNumber("AF183");
        segment.setBookingCode("ABCD");

        List<Leg> legs = new ArrayList<>();
        Leg leg = new Leg();
        leg.setOrigin(origin);
        leg.setDestination(destination);
        leg.setOperationDisclosure("DL 134");
        leg.setDuration(120);
        leg.setChangePlane(false);
        leg.setAircraft("A321");
        leg.setLocalDepartureTime(LocalDateTime.now());
        leg.setLocalArrivalTime(LocalDateTime.now().plusMinutes(120L));
        leg.setConnectionDuration(0);

        legs.add(leg);
        segment.setLegs(legs);
        slice.addSegment(segment);
        return slice;
    }
}
