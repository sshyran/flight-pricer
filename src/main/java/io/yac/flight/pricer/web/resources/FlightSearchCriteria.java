package io.yac.flight.pricer.web.resources;

import io.yac.flight.pricer.model.SliceSearchCriteria;

import java.util.ArrayList;
import java.util.List;

public class FlightSearchCriteria {


    private final Integer adultCount;
    private final Integer childCount;
    private final List<SliceSearchCriteria> slices;
    private final String ticketingCountry;
    private final Integer maximumSolutions;

    private FlightSearchCriteria(Integer adultCount, Integer childCount,
                                 List<SliceSearchCriteria> slices, String ticketingCountry,
                                 Integer maximumSolutions) {
        this.adultCount = adultCount;
        this.childCount = childCount;
        this.slices = slices;
        this.ticketingCountry = ticketingCountry;
        this.maximumSolutions = maximumSolutions;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Integer getAdultCount() {
        return adultCount;
    }

    public Integer getChildCount() {
        return childCount;
    }

    public List<SliceSearchCriteria> getSlices() {
        return slices;
    }

    public String getTicketingCountry() {
        return ticketingCountry;
    }

    public Integer getMaximumSolutions() {
        return maximumSolutions;
    }

    public static class Builder {
        private Integer adultCount;
        private Integer childCount;
        private List<SliceSearchCriteria> slices = new ArrayList<>();
        private String ticketingCountry;
        private Integer maximumSolutions;

        public Builder adultCount(Integer adultCount) {
            this.adultCount = adultCount;
            return this;
        }

        public Builder childCount(Integer childCount) {
            this.childCount = childCount;
            return this;
        }

        public Builder addSlice(SliceSearchCriteria slice) {
            this.slices.add(slice);
            return this;
        }

        public Builder ticketingCountry(String ticketingCountry) {
            this.ticketingCountry = ticketingCountry;
            return this;
        }

        public Builder maximumSolutions(Integer maximumSolutions) {
            this.maximumSolutions = maximumSolutions;
            return this;
        }

        public FlightSearchCriteria build() {
            return new FlightSearchCriteria(adultCount, childCount, slices, ticketingCountry, maximumSolutions);
        }
    }
}
