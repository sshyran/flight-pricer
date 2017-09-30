package io.yac.flight.pricer.web.controller;

import io.yac.flight.pricer.model.*;
import io.yac.flight.pricer.qpx.QPXResponse;
import io.yac.flight.pricer.qpx.SearchFlightService;
import io.yac.flight.pricer.web.resources.FlightSearchCriteria;
import io.yac.flight.pricer.web.resources.OneWayFlightSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/oneWayFlightSearches")
public class OneWayFlightSearchController {


    private static final String frontEndAppUrl = "http://localhost:4200";

    private final SearchFlightService searchFlightService;
    @Value("#{'${google.api.enabled:false}'}")

    private boolean isCallingQpxEnabled;

    @Autowired
    public OneWayFlightSearchController(SearchFlightService searchFlightService) {
        this.searchFlightService = searchFlightService;
    }


    @GetMapping(value = "")
    @CrossOrigin(origins = frontEndAppUrl)
    public OneWayFlightSearchResponse search(@RequestParam("departure") String departureAirport,
                                             @RequestParam("arrival") String arrivalAirport,
                                             @RequestParam("departureDate") String date,
                                             @RequestParam("numberOfAdult") Integer adultCount) {

        LocalDate departureDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        final FlightSearchCriteria searchCriteria = FlightSearchCriteria.builder().addSlice(
                SliceSearchCriteria.builder().origin(departureAirport).destination(arrivalAirport)
                        .departureDate(departureDate)
                        .build()).adultCount(adultCount).build();

        final QPXResponse qpxResponse = queryService(searchCriteria);

        OneWayFlightSearchResponse response = new OneWayFlightSearchResponse();
        response.setSolutions(qpxResponse.getSolutions());
        response.setOrigin(departureAirport);
        response.setDestination(arrivalAirport);
        response.setDepartureDate(date);
        return response;
    }

    private QPXResponse queryService(FlightSearchCriteria searchCriteria) {
        if (isCallingQpxEnabled) {
            return searchFlightService.searchFlights(searchCriteria);
        } else {


            List<Carrier> carriers = new ArrayList<>();
            List<Airport> airports = new ArrayList<>();
            List<City> cities = new ArrayList<>();

            List<Solution> solutions = new ArrayList<>();
            Solution solution = new Solution();
            solution.setPrices(Arrays.asList(Price.builder().amount(1234.0).country("US").currency("USD").build(),
                    Price.builder().amount(2902.3).country("FR").currency("EUR").build()));
            solution.setRefundable(false);
            List<Slice> slices = new ArrayList<>();
            Slice slice = new Slice();

            Segment segment = new Segment();
            segment.setCarrierIATA("AF");
            segment.setConnectionDuration(0);
            segment.setMarriedSegmentGroup("0");
            segment.setCabin(Cabin.ECONOMY);
            segment.setBookingCodeAvailableSeats(10);
            segment.setDuration(120);
            segment.setFlightNumber("AF183");
            segment.setBookingCode("ABCD");

            List<Leg> legs = new ArrayList<>();
            Leg leg = new Leg();
            leg.setOrigin(searchCriteria.getSlices().get(0).getOrigin());
            leg.setDestination(searchCriteria.getSlices().get(0).getDestination());
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
            slices.add(slice);
            solution.setSlices(slices);


            solutions.add(solution);
            return new QPXResponse(carriers, airports, cities, solutions);
        }
    }
}
