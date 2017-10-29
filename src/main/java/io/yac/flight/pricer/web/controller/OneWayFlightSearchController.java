package io.yac.flight.pricer.web.controller;

import io.yac.flight.pricer.model.Cabin;
import io.yac.flight.pricer.model.SliceSearchCriteria;
import io.yac.flight.pricer.qpx.QPXResponse;
import io.yac.flight.pricer.search.MultiCountrySearchHandler;
import io.yac.flight.pricer.web.resources.Airline;
import io.yac.flight.pricer.web.resources.FlightSearchCriteria;
import io.yac.flight.pricer.web.resources.OneWayFlightSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/oneWayFlightSearches")
public class OneWayFlightSearchController {


    private static final String frontEndAppUrl = "http://localhost:4200";

    private final MultiCountrySearchHandler searchFlightService;

    @Autowired
    public OneWayFlightSearchController(MultiCountrySearchHandler searchFlightService) {
        this.searchFlightService = searchFlightService;
    }


    @GetMapping(value = "")
    @CrossOrigin(origins = frontEndAppUrl)
    public OneWayFlightSearchResponse search(@RequestParam("departure") String departureAirport,
                                             @RequestParam("arrival") String arrivalAirport,
                                             @RequestParam("departureDate") String date,
                                             @RequestParam("numberOfAdult") Integer adultCount,
                                             @RequestParam(value = "numberOfChildren", defaultValue = "0") Integer childrenCount,
                                             @RequestParam(name = "currency", defaultValue = "EUR") String currency,
                                             @RequestParam(name = "travelClass", defaultValue = "Economy")
                                                     String travelClass) {

        LocalDate departureDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        final FlightSearchCriteria searchCriteria = FlightSearchCriteria.builder().addSlice(
                SliceSearchCriteria.builder().origin(departureAirport).destination(arrivalAirport)
                        .departureDate(departureDate)
                        .cabin(Cabin.fromExternalValue(travelClass))
                        .build()).adultCount(adultCount).childCount(childrenCount)
                .ticketingCountries(Arrays.asList("FR", "CA", "US", "CH", "NL", "GB")).currency(currency).build();

        final QPXResponse qpxResponse = queryService(searchCriteria);

        OneWayFlightSearchResponse response = new OneWayFlightSearchResponse();
        response.setSolutions(qpxResponse.getSolutions());
        response.setOrigin(departureAirport);
        response.setDestination(arrivalAirport);
        response.setDepartureDate(date);
        response.setAirlines(qpxResponse.getCarriers().stream().map(c -> new Airline(c.getIata(), c.getName())).collect(
                Collectors.toList()));

        return response;
    }

    private QPXResponse queryService(FlightSearchCriteria searchCriteria) {
        return searchFlightService.multiCountrySearch(searchCriteria);
    }
}
