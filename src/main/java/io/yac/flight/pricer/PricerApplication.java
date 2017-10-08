package io.yac.flight.pricer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
public class PricerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PricerApplication.class, args);
    }

}
