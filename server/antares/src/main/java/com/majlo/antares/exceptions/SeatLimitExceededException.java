package com.majlo.antares.exceptions;

public class SeatLimitExceededException extends RuntimeException {
    public SeatLimitExceededException(String message) {
        super(message);
    }
}

