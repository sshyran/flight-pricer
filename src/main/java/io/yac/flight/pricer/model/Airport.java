package io.yac.flight.pricer.model;

import java.util.Objects;

public class Airport {

    private final String city;
    private final String name;

    private final String iata;

    public Airport(String iata, String name, String city) {

        this.iata = iata;
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getIata() {
        return iata;
    }

    public String getCity() {
        return city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Airport airport = (Airport) o;
        return Objects.equals(iata, airport.iata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iata);
    }
}
