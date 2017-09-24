package io.yac.flight.pricer.qpx;

import io.yac.flight.pricer.model.Airport;
import io.yac.flight.pricer.model.Carrier;
import io.yac.flight.pricer.model.City;
import io.yac.flight.pricer.model.Solution;

import java.util.List;

public class QPXResponse {

    private final List<Carrier> carriers;

    private final List<Airport> airports;

    private final List<City> cities;

    private final List<Solution> solutions;

    public QPXResponse(List<Carrier> carriers, List<Airport> airports,
                       List<City> cities, List<Solution> solutions) {
        this.carriers = carriers;
        this.airports = airports;
        this.cities = cities;
        this.solutions = solutions;
    }

    public List<Carrier> getCarriers() {
        return carriers;
    }

    public List<Airport> getAirports() {
        return airports;
    }

    public List<City> getCities() {
        return cities;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }
}
