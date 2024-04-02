package com.example.hotel.service.reservation;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(String message) {
        super(message);
    }

    public ReservationNotFoundException() {
        super();
    }

}
