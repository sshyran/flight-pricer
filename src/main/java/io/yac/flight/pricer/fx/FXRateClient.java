package io.yac.flight.pricer.fx;

import java.math.BigDecimal;

public interface FXRateClient {


    BigDecimal getRate(String baseCurrency, String quoteCurrency);

    void resetCache();

}
