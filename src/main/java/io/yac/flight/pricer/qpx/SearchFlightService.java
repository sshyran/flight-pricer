package io.yac.flight.pricer.qpx;

import io.yac.flight.pricer.web.resources.FlightSearchCriteria;

public interface SearchFlightService {
    QPXResponse searchFlights(FlightSearchCriteria flightSearchCriteria);
}
