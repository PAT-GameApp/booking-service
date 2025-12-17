package com.cognizant.BookingService.kafka.event;

public final class BookingEventType {
    public static final String CREATED = "CREATED";
    public static final String UPDATED = "UPDATED";
    public static final String DELETED = "DELETED";
    public static final String ALLOTTED = "ALLOTTED";
    
    private BookingEventType() {
        // Prevent instantiation
    }
}
