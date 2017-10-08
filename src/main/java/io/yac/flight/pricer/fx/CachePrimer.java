package io.yac.flight.pricer.fx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CachePrimer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(CachePrimer.class);

    private final FXRateClient fxRateClient;

    @Autowired
    public CachePrimer(FXRateClient fxRateClient) {
        this.fxRateClient = fxRateClient;
    }


    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Priming exchange rate cache");
        fxRateClient.resetCache();
        LOGGER.info("Exchange rate cache primed");
    }
}
