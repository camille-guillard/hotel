package com.example.hotel.service.reservation;

import com.example.hotel.dto.ReservationDto;
import com.example.hotel.dto.ReservationRequestDto;

import java.time.LocalDate;
import java.util.List;

public interface IReservationService {

    List<ReservationDto> getReservations(LocalDate startDate, LocalDate endDate);

    Long createReservation(ReservationRequestDto reservation) throws ReservationException;

    ReservationDto getReservationById(Long reservationId) throws ReservationNotFoundException;
}
