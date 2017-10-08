package io.yac.flight.pricer.scheduler;

import io.yac.flight.pricer.fx.CachePrimer;
import io.yac.flight.pricer.fx.FXRateClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(CachePrimer.class);

    private final FXRateClient fxRateClient;

    @Autowired
    public ScheduledTask(FXRateClient fxRateClient) {
        this.fxRateClient = fxRateClient;
    }

    @Scheduled(cron = "0 20 16 * * MON-FRI", zone = "CET")
    public void resetFxRateCache() {
        LOGGER.info("Resetting fx rate cache");
        fxRateClient.resetCache();
        LOGGER.info("FX rate cache reset");
    }

}
