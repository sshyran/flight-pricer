package io.yac.flight.pricer.model;

import java.util.List;

public class Segment {

    private Integer duration;

    private String carrierIATA;

    private String number;

    private Cabin cabin;

    private String bookingCode;

    private Integer bookingCodeAvailableSeats;

    private String marriedSegmentGroup;

    private List<Leg> legs;

    private Integer connectionDuration;

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getCarrierIATA() {
        return carrierIATA;
    }

    public void setCarrierIATA(String carrierIATA) {
        this.carrierIATA = carrierIATA;
    }

    public String getNumber() {
        return number;
    }

    public void setFlightNumber(String number) {
        this.number = number;
    }

    public Cabin getCabin() {
        return cabin;
    }

    public void setCabin(Cabin cabin) {
        this.cabin = cabin;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public Integer getBookingCodeAvailableSeats() {
        return bookingCodeAvailableSeats;
    }

    public void setBookingCodeAvailableSeats(Integer bookingCodeAvailableSeats) {
        this.bookingCodeAvailableSeats = bookingCodeAvailableSeats;
    }

    public String getMarriedSegmentGroup() {
        return marriedSegmentGroup;
    }

    public void setMarriedSegmentGroup(String marriedSegmentGroup) {
        this.marriedSegmentGroup = marriedSegmentGroup;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public Integer getConnectionDuration() {
        return connectionDuration;
    }

    public void setConnectionDuration(Integer connectionDuration) {
        this.connectionDuration = connectionDuration;
    }
}
