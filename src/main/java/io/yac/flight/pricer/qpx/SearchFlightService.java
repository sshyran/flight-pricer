package io.yac.flight.pricer.qpx;

import io.yac.flight.pricer.web.model.FlightSearchCriteria;

public interface SearchFlightService {
    QPXResponse searchFlights(FlightSearchCriteria flightSearchCriteria);
}
