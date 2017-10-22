package io.yac.flight.pricer.web.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Airline {

    @JsonProperty(value = "id")
    private String carrierIATA;

    private String name;

    public Airline() {
    }

    public Airline(String carrierIATA, String name) {
        this.carrierIATA = carrierIATA;
        this.name = name;
    }

    public String getCarrierIATA() {
        return carrierIATA;
    }

    public void setCarrierIATA(String carrierIATA) {
        this.carrierIATA = carrierIATA;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
