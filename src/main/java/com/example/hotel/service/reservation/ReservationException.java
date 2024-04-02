package com.example.hotel.service.reservation;

public class ReservationException extends RuntimeException {

    public ReservationException(String message) {
        super(message);
    }

    public ReservationException() {
        super();
    }

}