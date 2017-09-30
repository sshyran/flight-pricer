package io.yac.flight.pricer.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Solution {
    private final String id;

    private List<Price> prices = new ArrayList<>();

    private List<Slice> slices;

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

}
