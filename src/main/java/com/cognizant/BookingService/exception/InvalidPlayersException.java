package com.cognizant.BookingService.exception;

public class InvalidPlayersException extends RuntimeException {
    public InvalidPlayersException(String message) {
        super(message);
    }
}
