package io.yac.flight.pricer.model;

public class Carrier {

    private String iata;

    private String name;

    public Carrier(String iata, String name) {

        this.iata = iata;
        this.name = name;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
