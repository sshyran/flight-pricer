package io.yac.flight.pricer.web.controller;

import io.yac.flight.pricer.qpx.QPXResponse;
import io.yac.flight.pricer.qpx.SearchFlightService;
import io.yac.flight.pricer.web.model.FlightSearchCriteria;
import io.yac.flight.pricer.web.model.SearchFlightResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchFlightController {

    private final SearchFlightService searchFlightService;

    @Autowired
    public SearchFlightController(SearchFlightService searchFlightService) {
        this.searchFlightService = searchFlightService;
    }

    @RequestMapping(value = "/search/flights", method = RequestMethod.POST)
    public SearchFlightResponse search(@RequestBody FlightSearchCriteria searchCriteria) {
        QPXResponse qpxResponse = searchFlightService.searchFlights(searchCriteria);
        return null;
    }

}
