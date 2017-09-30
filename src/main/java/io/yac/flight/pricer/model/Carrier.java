package io.yac.flight.pricer.model;

import java.util.Objects;

public class Carrier {

    private final String iata;

    private final String name;

    public Carrier(String iata, String name) {

        this.iata = iata;
        this.name = name;
    }

    public String getIata() {
        return iata;
    }

    public String getName() {
        return name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Carrier carrier = (Carrier) o;
        return Objects.equals(iata, carrier.iata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iata);
    }
}
