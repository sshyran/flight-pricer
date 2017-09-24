package io.yac.flight.pricer.web.resources;

public class OneWayFlightSearchQuery implements SearchFlightQuery {
    private String departureAirport;
    private String arrivalAirport;
    private String date;

    public OneWayFlightSearchQuery(String departureAirport, String arrivalAirport, String date) {
        super();
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.date = date;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
