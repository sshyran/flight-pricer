package io.yac.flight.pricer.qpx;

public interface SearchFlightService {
    QPXResponse searchFlights(QPXSearchCriteria searchCriteria, String ticketingCountry);
}
