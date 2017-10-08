package io.yac.flight.pricer.model;

import java.util.UUID;

public class Price {

    private final String id;

    private String currency;

    private Double amount;

    private String requestCurrency;

    private Double requestCurrencyAmount;

    private String country;

    public Price() {
        this.id = UUID.randomUUID().toString();
    }

    private Price(String currency, Double amount, String country, String requestCurrency,
                  Double requestCurrencyAmount) {
        this();
        this.currency = currency;
        this.amount = amount;
        this.country = country;
        this.requestCurrency = requestCurrency;
        this.requestCurrencyAmount = requestCurrencyAmount;
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

    public String getRequestCurrency() {
        return requestCurrency;
    }

    public void setRequestCurrency(String requestCurrency) {
        this.requestCurrency = requestCurrency;
    }

    public Double getRequestCurrencyAmount() {
        return requestCurrencyAmount;
    }

    public void setRequestCurrencyAmount(Double requestCurrencyAmount) {
        this.requestCurrencyAmount = requestCurrencyAmount;
    }

    public static class Builder {

        private String currency;
        private Double amount;
        private String country;
        private String requestCurrency;
        private Double requestCurrencyAmount;

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

        public Builder requestCurrency(String requestCurrency) {
            this.requestCurrency = requestCurrency;
            return this;
        }

        public Builder requestCurrencyAmount(Double requestCurrencyAmount) {
            this.requestCurrencyAmount = requestCurrencyAmount;
            return this;
        }


        public Price build() {
            return new Price(currency, amount, country, requestCurrency, requestCurrencyAmount);
        }
    }
}
