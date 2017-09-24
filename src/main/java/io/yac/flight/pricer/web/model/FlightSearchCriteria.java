package io.yac.flight.pricer.web.model;

import io.yac.flight.pricer.model.SliceSearchCriteria;

import java.util.List;

public class FlightSearchCriteria {


    private Integer adultCount;
    private Integer childCount;
    private List<SliceSearchCriteria> slices;
    private String ticketingCountry;
    private Integer maximumSolutions;

    public Integer getAdultCount() {
        return adultCount;
    }

    public void setAdultCount(Integer adultCount) {
        this.adultCount = adultCount;
    }

    public Integer getChildCount() {
        return childCount;
    }

    public void setChildCount(Integer childCount) {
        this.childCount = childCount;
    }

    public List<SliceSearchCriteria> getSlices() {
        return slices;
    }

    public void setSlices(List<SliceSearchCriteria> slices) {
        this.slices = slices;
    }

    public String getTicketingCountry() {
        return ticketingCountry;
    }

    public void setTicketingCountry(String ticketingCountry) {
        this.ticketingCountry = ticketingCountry;
    }

    public Integer getMaximumSolutions() {
        return maximumSolutions;
    }

    public void setMaximumSolutions(Integer maximumSolutions) {
        this.maximumSolutions = maximumSolutions;
    }
}
