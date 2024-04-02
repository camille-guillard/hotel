package com.example.hotel.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationRequestDto {
    @NotNull
    private LocalDate reservationStartDate;
    @NotNull
    private LocalDate reservationEndDate;
    @NotNull
    private Long roomId;
    @NotNull
    private Long bookerId;
    @NotNull
    private Double price;
    @NotEmpty
    private List<Long> customerIds;

}
