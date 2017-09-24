package io.yac.flight.pricer.exceptions;

import java.io.IOException;

public class DependentServiceException extends RuntimeException {
    public DependentServiceException(IOException e) {
    }
}
