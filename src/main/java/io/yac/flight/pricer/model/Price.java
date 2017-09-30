package io.yac.flight.pricer.model;

import java.util.UUID;

public class Price {

    private final String id;

    private String currency;

    private Double amount;

    private String country;

    public Price() {
        this.id = UUID.randomUUID().toString();
    }

    public Price(String currency, Double amount, String country) {
        this();
        this.currency = currency;
        this.amount = amount;
        this.country = country;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public static class Builder {

        private String currency;
        private Double amount;
        private String country;

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder amount(Double amount) {
            this.amount = amount;
            return this;
        }

        public Builder country(String country) {
            this.country = country;
            return this;
        }

        public Price build() {
            return new Price(currency, amount, country);
        }
    }
}
