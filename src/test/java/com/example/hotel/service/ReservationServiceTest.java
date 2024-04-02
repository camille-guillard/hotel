package com.example.hotel.service;

import com.example.hotel.dto.ReservationDto;
import com.example.hotel.dto.ReservationRequestDto;
import com.example.hotel.entity.CustomerEntity;
import com.example.hotel.entity.ReservationEntity;
import com.example.hotel.entity.RoomEntity;
import com.example.hotel.mapper.ReservationMapper;
import com.example.hotel.repository.CustomerRepository;
import com.example.hotel.repository.ReservationRepository;
import com.example.hotel.repository.RoomRepository;
import com.example.hotel.service.reservation.ReservationException;
import com.example.hotel.service.reservation.ReservationNotFoundException;
import com.example.hotel.service.reservation.ReservationServiceImpl;
import com.example.hotel.service.reservation.ReservationValidator;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @Spy
    @Autowired
    private Validator validator;

    @Spy
    @InjectMocks
    private ReservationValidator reservationValidator = new ReservationValidator(validator);

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    public void testGetReservations() {
        List<ReservationEntity> reservationEntities = new ArrayList<>();
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setId(1L);
        reservationEntity.setPrice(100.00);
        reservationEntities.add(reservationEntity);
        when(reservationRepository.findByStartDateAndEndDate(any(LocalDate.class), any(LocalDate.class))).thenReturn(reservationEntities);

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(1L);
        reservationDto.setPrice(100.00);
        when(reservationMapper.entityToDto(eq(reservationEntity))).thenReturn(reservationDto);


        List<ReservationDto> result = reservationService.getReservations(LocalDate.now(), LocalDate.now());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertNotNull(result.get(0));
        assertEquals(1L, result.get(0).getId().longValue());
        assertEquals(100.00, result.get(0).getPrice(), 0);
    }

    @Test
    public void testGetReservationByUserIdShouldFail() throws ReservationNotFoundException {
        List<ReservationDto> result = reservationService.getReservations(LocalDate.now(), LocalDate.now());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetReservationById() throws ReservationNotFoundException {
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setId(1L);
        reservationEntity.setPrice(100.00);
        when(reservationRepository.findById(eq(1L))).thenReturn(Optional.of(reservationEntity));

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(1L);
        reservationDto.setPrice(100.00);
        when(reservationMapper.entityToDto(eq(reservationEntity))).thenReturn(reservationDto);


        ReservationDto result = reservationService.getReservationById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId().longValue());
        assertEquals(100.00, result.getPrice(), 0);
    }

    @Test(expected = ReservationNotFoundException.class)
    public void testGetReservationByIdShouldFail() throws ReservationNotFoundException {
        reservationService.getReservationById(999L);
    }

    @Test(expected = ReservationException.class)
    public void testCreateReservationShouldFailOnFetchCustomer() throws ReservationException {
        ReservationRequestDto reservationDto = new ReservationRequestDto();
        reservationDto.setPrice(100.00);
        reservationDto.setBookerId(1L);
        reservationDto.setRoomId(1L);
        reservationDto.setReservationStartDate(LocalDate.of(2024,1,1));
        reservationDto.setReservationEndDate(LocalDate.of(2024,1,2));
        reservationDto.setCustomerIds(List.of(1L, 2L));

        reservationService.createReservation(reservationDto);
    }

    @Test(expected = ReservationException.class)
    public void testCreateReservationShouldFailOnFetchRoom() throws ReservationException {
        when(customerRepository.findById(eq(1L))).thenReturn(Optional.of(new CustomerEntity()));

        ReservationRequestDto reservationDto = new ReservationRequestDto();
        reservationDto.setPrice(100.00);
        reservationDto.setBookerId(1L);
        reservationDto.setRoomId(1L);
        reservationDto.setReservationStartDate(LocalDate.of(2024,1,1));
        reservationDto.setReservationEndDate(LocalDate.of(2024,1,2));
        reservationDto.setCustomerIds(List.of(1L, 2L));

        reservationService.createReservation(reservationDto);
    }

    @Test(expected = ReservationException.class)
    public void testCreateReservationShouldFailOnValidator() throws ReservationException {
        ReservationRequestDto reservationDto = new ReservationRequestDto();
        reservationDto.setPrice(100.00);
        reservationDto.setBookerId(1L);
        reservationDto.setRoomId(1L);
        reservationDto.setReservationStartDate(LocalDate.of(2024,1,1));
        reservationDto.setReservationEndDate(LocalDate.of(2024,1,2));
        reservationDto.setCustomerIds(List.of(1L, 2L));

        reservationService.createReservation(reservationDto);
    }

    @Test
    public void testCreateReservationShouldSuccess() throws ReservationException {
        when(customerRepository.findById(eq(1L))).thenReturn(Optional.of(new CustomerEntity()));
        when(roomRepository.findById(eq(1L))).thenReturn(Optional.of(new RoomEntity()));

        ReservationRequestDto reservationDto = new ReservationRequestDto();
        reservationDto.setBookerId(1L);
        reservationDto.setRoomId(1L);
        reservationDto.setReservationStartDate(LocalDate.of(2024,1,1));
        reservationDto.setReservationEndDate(LocalDate.of(2024,1,2));
        reservationDto.setCustomerIds(List.of(1L, 2L));

        ReservationEntity entity = new ReservationEntity();
        entity.setId(1L);
        when(reservationRepository.saveAndFlush(any())).thenReturn(entity);

        Long result = reservationService.createReservation(reservationDto);
        assertEquals( 1L, result.longValue());
    }

}
