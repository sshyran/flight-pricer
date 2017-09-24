package io.yac.flight.pricer.model;

public class Airport {

    private final String city;
    private String name;
    private String cityIataCode;

    private String iata;

    public Airport(String iata, String name, String city) {

        this.iata = iata;
        this.name = name;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCityIataCode() {
        return cityIataCode;
    }

    public void setCityIataCode(String cityIataCode) {
        this.cityIataCode = cityIataCode;
    }

    public String getIata() {
        return iata;
    }

    public void setIata(String iata) {
        this.iata = iata;
    }
}
