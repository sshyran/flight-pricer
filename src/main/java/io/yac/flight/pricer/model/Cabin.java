package io.yac.flight.pricer.model;

import java.util.Arrays;

public enum Cabin {
    ECONOMY("COACH"), PREMIUM("PREMIUM_COACH"), BUSINESS("BUSINESS"), FIRST("FIRST");

    private final String qpxValue;

    Cabin(String qpxValue) {
        this.qpxValue = qpxValue;
    }

    public static Cabin fromQpxResponse(String cabin) {
        return Arrays.stream(Cabin.values()).filter((c) -> c.qpxValue.equalsIgnoreCase(cabin)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported cabin " + cabin));
    }

    String getQpxValue() {
        return qpxValue;
    }
}
