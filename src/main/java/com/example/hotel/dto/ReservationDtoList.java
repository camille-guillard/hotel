package com.example.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ReservationDtoList {

    private List<ReservationDto> reservations;

    public ReservationDtoList() {
        reservations = new ArrayList<>();
    }

}