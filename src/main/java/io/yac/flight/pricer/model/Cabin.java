package io.yac.flight.pricer.model;

import java.util.Arrays;

public enum Cabin {
    ECONOMY("COACH", "ECONOMY"), PREMIUM("PREMIUM_COACH", "PREMIUM"), BUSINESS("BUSINESS", "BUSINESS"), FIRST("FIRST",
            "FIRST");

    private final String qpxValue;
    private final String externalValue;

    Cabin(String qpxValue, String externalValue) {
        this.qpxValue = qpxValue;
        this.externalValue = externalValue;
    }

    public static Cabin fromQpxResponse(String cabin) {
        return Arrays.stream(Cabin.values()).filter((c) -> c.qpxValue.equalsIgnoreCase(cabin)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported cabin " + cabin));
    }

    public static Cabin fromExternalValue(String externalValue) {
        return Arrays.stream(Cabin.values()).filter(c -> c.externalValue.equalsIgnoreCase(externalValue)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported cabin " + externalValue));
    }

    public String getQpxValue() {
        return qpxValue;
    }

    public String getExternalValue() {
        return externalValue;
    }
}
