package com.example.hotel.controller;

import com.example.hotel.dto.ReservationDto;
import com.example.hotel.dto.ReservationDtoList;
import com.example.hotel.dto.ReservationRequestDto;
import com.example.hotel.service.reservation.IReservationService;
import com.example.hotel.service.reservation.ReservationException;
import com.example.hotel.service.reservation.ReservationNotFoundException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import io.swagger.annotations.Api;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@Api(tags = "hotel-reservations")
@RequestMapping(value = "/api/reservations", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class ReservationController {
    private final IReservationService reservationService;

    @Autowired
    public ReservationController(IReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @ApiOperation(value = "Fetch reservations by user id")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ReservationDtoList getReservationsByDates(@RequestParam(required=false) LocalDate startDate,
                                                     @RequestParam(required=false) LocalDate endDate) {
        //SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ReservationDtoList(reservationService.getReservations(startDate, endDate));
    }

    @ApiOperation(value = "Fetch reservation by id")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ReservationDto getReservationById(Principal principal, @PathVariable Long id) throws ReservationNotFoundException {
        return reservationService.getReservationById(id);
    }

    @ApiOperation(value = "Fetch reservation by user id")
    @PostMapping(value = "/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createReservation(@RequestBody ReservationRequestDto reservationRequestDto) throws ReservationException {
        return reservationService.createReservation(reservationRequestDto);
    }

}
