package io.yac.flight.pricer.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Leg {

    private final String id;

    private String aircraft;

    private LocalDateTime localArrivalTime;

    private LocalDateTime localDepartureTime;

    private String origin;

    private String destination;

    private Integer duration;

    private String operationDisclosure;

    private Integer connectionDuration;

    private boolean changePlane;

    public Leg() {
        this.id = UUID.randomUUID().toString();
    }

    public String getAircraft() {
        return aircraft;
    }

    public void setAircraft(String aircraft) {
        this.aircraft = aircraft;
    }

    public LocalDateTime getLocalArrivalTime() {
        return localArrivalTime;
    }

    public void setLocalArrivalTime(LocalDateTime localArrivalTime) {
        this.localArrivalTime = localArrivalTime;
    }

    public LocalDateTime getLocalDepartureTime() {
        return localDepartureTime;
    }

    public void setLocalDepartureTime(LocalDateTime localDepartureTime) {
        this.localDepartureTime = localDepartureTime;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getOperationDisclosure() {
        return operationDisclosure;
    }

    public void setOperationDisclosure(String operationDisclosure) {
        this.operationDisclosure = operationDisclosure;
    }

    public Integer getConnectionDuration() {
        return connectionDuration;
    }

    public void setConnectionDuration(Integer connectionDuration) {
        this.connectionDuration = connectionDuration;
    }

    public boolean isChangePlane() {
        return changePlane;
    }

    public void setChangePlane(Boolean changePlane) {
        this.changePlane = changePlane == null ? false : changePlane;
    }

    public String getId() {
        return id;
    }

}
