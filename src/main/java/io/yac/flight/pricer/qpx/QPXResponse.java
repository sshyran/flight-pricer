package io.yac.flight.pricer.qpx;

import io.yac.flight.pricer.model.Airport;
import io.yac.flight.pricer.model.Carrier;
import io.yac.flight.pricer.model.City;
import io.yac.flight.pricer.model.Solution;

import java.util.List;
import java.util.Set;

public class QPXResponse {

    private final Set<Carrier> carriers;

    private final Set<Airport> airports;

    private final Set<City> cities;

    private final List<Solution> solutions;

    public QPXResponse(Set<Carrier> carriers, Set<Airport> airports,
                       Set<City> cities, List<Solution> solutions) {
        this.carriers = carriers;
        this.airports = airports;
        this.cities = cities;
        this.solutions = solutions;
    }

    public Set<Carrier> getCarriers() {
        return carriers;
    }

    public Set<Airport> getAirports() {
        return airports;
    }

    public Set<City> getCities() {
        return cities;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }
}
