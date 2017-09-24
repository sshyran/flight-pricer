package io.yac.flight.pricer.model;

public class City {

    private String name;

    private String iata;

    private String country;

    public City(String iata, String name, String country) {

        this.iata = iata;
        this.name = name;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
