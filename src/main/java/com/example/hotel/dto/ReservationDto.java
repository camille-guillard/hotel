package com.example.hotel.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationDto {
    private Long id;
    private LocalDate reservationStartDate;
    private LocalDate reservationEndDate;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;
    private RoomDto room;
    private CustomerDto booker;
    private Double price;
    private Set<CustomerDto> customers;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if( ! (obj instanceof ReservationDto) ) return false;

        ReservationDto other = (ReservationDto) obj;

        return this.id !=null ? this.id.equals(other.id) : this.id == other.id;
    }

    @Override
    public int hashCode() {
        return price.intValue();
    }
}
