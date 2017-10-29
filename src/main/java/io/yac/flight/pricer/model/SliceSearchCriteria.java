package io.yac.flight.pricer.model;

import java.time.LocalDate;

public class SliceSearchCriteria {
    private final String origin;
    private final String destination;
    private final Cabin cabin;
    private LocalDate departureDate;

    private SliceSearchCriteria(String origin, String destination, Cabin cabin, LocalDate departureDate) {
        this.origin = origin;
        this.destination = destination;
        this.cabin = cabin;
        this.departureDate = departureDate;
    }

    public static Builder builder() {
        return new Builder();
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

    public static class Builder {
        private String origin;
        private String destination;
        private Cabin cabin;
        private LocalDate departureDate;

        public Builder origin(String origin) {
            this.origin = origin;
            return this;
        }

        public Builder destination(String destination) {
            this.destination = destination;
            return this;
        }

        public Builder cabin(Cabin cabin) {
            this.cabin = cabin;
            return this;
        }

        public Builder departureDate(LocalDate departureDate) {
            this.departureDate = departureDate;
            return this;
        }

        public SliceSearchCriteria build() {
            return new SliceSearchCriteria(origin, destination, cabin, departureDate);
        }
    }
}
