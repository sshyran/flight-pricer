package io.yac.flight.pricer.qpx;

import io.yac.flight.pricer.model.Cabin;
import io.yac.flight.pricer.model.SliceSearchCriteria;

import java.time.LocalDate;

public class QPXSliceSearchCriteria {
    private final String origin;
    private final String destination;
    private final Cabin cabin;
    private final LocalDate departureDate;

    private QPXSliceSearchCriteria(String origin, String destination, Cabin cabin, LocalDate departureDate) {
        this.origin = origin;
        this.destination = destination;
        this.cabin = cabin;
        this.departureDate = departureDate;
    }

    public static QPXSliceSearchCriteria from(SliceSearchCriteria sliceSearchCriteria) {
        return new QPXSliceSearchCriteria(sliceSearchCriteria.getOrigin(), sliceSearchCriteria.getDestination(),
                sliceSearchCriteria.getCabin(), sliceSearchCriteria.getDepartureDate());
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public Cabin getCabin() {
        return cabin;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }
}
