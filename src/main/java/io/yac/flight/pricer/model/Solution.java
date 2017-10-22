package io.yac.flight.pricer.model;

import java.util.*;

public class Solution {
    private final String id;

    private List<Price> prices = new ArrayList<>();

    private List<Slice> slices;

    private Set<String> airlines = new HashSet<>();

    private boolean isRefundable;

    public Solution() {
        this.id = UUID.randomUUID().toString();
    }

    public List<Price> getPrices() {
        return prices;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    public void addPrice(Price price) {
        this.prices.add(price);
    }

    public List<Slice> getSlices() {
        return slices;
    }

    public void setSlices(List<Slice> slices) {
        this.slices = slices;
        this.airlines = new HashSet<>();
        slices.forEach(slice -> airlines.addAll(slice.getAirlinesIATA()));
    }

    public boolean isRefundable() {
        return isRefundable;
    }

    public void setRefundable(boolean refundable) {
        isRefundable = refundable;
    }

    public String getId() {
        return id;
    }

    public Set<String> getAirlines() {
        return airlines;
    }
}
