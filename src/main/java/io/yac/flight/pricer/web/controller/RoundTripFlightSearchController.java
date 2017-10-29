package io.yac.flight.pricer.web.controller;

import io.yac.flight.pricer.model.Cabin;
import io.yac.flight.pricer.model.SliceSearchCriteria;
import io.yac.flight.pricer.qpx.QPXResponse;
import io.yac.flight.pricer.search.MultiCountrySearchHandler;
import io.yac.flight.pricer.web.resources.Airline;
import io.yac.flight.pricer.web.resources.FlightSearchCriteria;
import io.yac.flight.pricer.web.resources.RoundTripFlightSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/roundTripFlightSearches")
public class RoundTripFlightSearchController {


    private static final String frontEndAppUrl = "http://localhost:4200";

    private final MultiCountrySearchHandler searchFlightService;

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
                                                @RequestParam(value = "numberOfChildren", defaultValue = "0")
                                                        Integer childCount,
                                                @RequestParam(value = "currency", defaultValue = "EUR")
                                                        String currency,
                                                @RequestParam(name = "travelClass", defaultValue = "Economy")
                                                        String travelClass) {

        LocalDate departureDate = LocalDate.parse(departureDateStr, DateTimeFormatter.ISO_DATE);
        LocalDate returnDate = LocalDate.parse(returnDateStr, DateTimeFormatter.ISO_DATE);

        Cabin cabin = Cabin.fromExternalValue(travelClass);
        final SliceSearchCriteria outwardJourney =
                SliceSearchCriteria.builder().origin(departureAirport).destination(arrivalAirport)
                        .departureDate(departureDate).cabin(cabin)
                        .build();

        final SliceSearchCriteria returnJourney =
                SliceSearchCriteria.builder().origin(arrivalAirport).destination(departureAirport)
                        .departureDate(returnDate).cabin(cabin).build();


        final FlightSearchCriteria searchCriteria =
                FlightSearchCriteria.builder().addSlice(outwardJourney).addSlice(returnJourney).adultCount(adultCount)
                        .childCount(childCount)
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
        return searchFlightService.multiCountrySearch(searchCriteria);
    }

}
