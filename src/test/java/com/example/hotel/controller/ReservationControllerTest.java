package com.example.hotel.controller;

import com.example.hotel.dto.ReservationDto;
import com.example.hotel.dto.ReservationRequestDto;
import com.example.hotel.service.reservation.IReservationService;
import com.example.hotel.service.reservation.ReservationException;
import com.example.hotel.service.reservation.ReservationNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = {ReservationController.class}, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private RestExceptionHandler restProcessingExceptionThrowingController;

    @MockBean
    private IReservationService reservationService;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(restProcessingExceptionThrowingController)
                .setControllerAdvice(new RestExceptionHandler())
                .apply(springSecurity())
                .build();
    }


    @Test
    void getReservationsWithStartDateAndEndDateShouldSuccess() throws Exception {
        List<ReservationDto> reservations = new ArrayList<>();
        ReservationDto r = new ReservationDto();
        r.setPrice(100.00);
        r.setCreationDate(LocalDateTime.now());
        reservations.add(r);

        doReturn(reservations).when(reservationService).getReservations(any(LocalDate.class), any(LocalDate.class));

        this.mockMvc.perform(get("/api/reservations?startDate=2024-01-01&endDate=2024-01-01").with(csrf()))
                .andDo(print())
                .andExpect(content().string(StringContains.containsString("100")));
    }

    @Test
    void getReservationsWithoutStartDateAndEndDateShouldSuccess2() throws Exception {
        List<ReservationDto> reservations = new ArrayList<>();
        ReservationDto r = new ReservationDto();
        r.setPrice(100.00);
        r.setCreationDate(LocalDateTime.now());
        reservations.add(r);

        doReturn(reservations).when(reservationService).getReservations(any(), any());

        this.mockMvc.perform(get("/api/reservations").with(csrf()))
                .andDo(print())
                .andExpect(content().string(StringContains.containsString("100")));
    }

    @Test
    void getReservationsShouldReturnBadRequest() throws Exception {
        doThrow(new IllegalArgumentException()).when(reservationService).getReservations(any(), any());

        this.mockMvc.perform(get("/api/reservations").with(csrf()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void createReservationShouldSuccess() throws Exception {
        doReturn(1L).when(reservationService).createReservation(any());

        ReservationRequestDto reservationDto = new ReservationRequestDto();
        reservationDto.setPrice(100.00);
        reservationDto.setBookerId(1L);
        reservationDto.setRoomId(1L);
        reservationDto.setReservationStartDate(LocalDate.of(2024,1,1));
        reservationDto.setReservationEndDate(LocalDate.of(2024,1,2));
        reservationDto.setCustomerIds(List.of(1L, 2L));
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String body = mapper.writeValueAsString(reservationDto);

        this.mockMvc.perform(post("/api/reservations/new")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void createReservationShouldReturnInternalServerError() throws Exception {
        doThrow(new ReservationException()).when(reservationService).createReservation(any());

        ReservationRequestDto reservation = new ReservationRequestDto();
        ObjectMapper mapper = new ObjectMapper();
        String body = mapper.writeValueAsString(reservation);

        this.mockMvc.perform(post("/api/reservations/new")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getReservationByIdShouldSuccess() throws Exception {
        ReservationDto reservation = new ReservationDto();
        reservation.setPrice(100.00);
        reservation.setCreationDate(LocalDateTime.now());

        doReturn(reservation).when(reservationService).getReservationById(eq(1L));

        this.mockMvc.perform(get("/api/reservations/1").with(csrf()))
                .andDo(print())
                .andExpect(content().string(StringContains.containsString("100")));
    }

    @Test
    void getReservationByIdShouldReturnNotFound() throws Exception {
        doThrow(new ReservationNotFoundException()).when(reservationService).getReservationById(eq(9999L));

        this.mockMvc.perform(get("/api/reservations/9999").with(csrf()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


}
