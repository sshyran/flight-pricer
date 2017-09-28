package io.yac.flight.pricer.exceptions;

public class DependentServiceException extends RuntimeException {
    public DependentServiceException(Exception e) {
        super(e);
    }
}
