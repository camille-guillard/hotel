package com.example.hotel.service.reservation;

import com.example.hotel.dto.ReservationDto;
import com.example.hotel.dto.ReservationRequestDto;
import com.example.hotel.entity.CustomerEntity;
import com.example.hotel.entity.ReservationEntity;
import com.example.hotel.entity.RoomEntity;
import com.example.hotel.mapper.ReservationMapper;
import com.example.hotel.repository.CustomerRepository;
import com.example.hotel.repository.ReservationRepository;
import com.example.hotel.repository.RoomRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements IReservationService {

    private final ReservationRepository reservationRepository;

    private final CustomerRepository customerRepository;

    private final RoomRepository roomRepository;

    private final ReservationMapper reservationMapper;

    private final ReservationValidator validator;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  CustomerRepository customerRepository,
                                  RoomRepository roomRepository,
                                  ReservationMapper reservationMapper,
                                  ReservationValidator validator) {
        this.reservationRepository = reservationRepository;
        this.customerRepository = customerRepository;
        this.roomRepository = roomRepository;
        this.reservationMapper = reservationMapper;
        this.validator = validator;
    }

    @Override
    @Transactional
    public List<ReservationDto> getReservations(LocalDate startDate, LocalDate endDate) {
        return reservationRepository.findByStartDateAndEndDate(startDate, endDate)
                .stream()
                .map(reservationMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Long createReservation(ReservationRequestDto reservation) throws ReservationException {
        validator.validate(reservation);

        CustomerEntity booker = customerRepository.findById(reservation.getBookerId())
                .orElseThrow(() -> new ReservationException("Customer with id " + reservation.getBookerId() + " not found !"));


        RoomEntity room = roomRepository.findById(reservation.getRoomId())
                .orElseThrow(() -> new ReservationException("Room with id " + reservation.getRoomId() + " not found !"));

        ReservationEntity entity = new ReservationEntity();
        entity.setReservationStartDate(reservation.getReservationStartDate());
        entity.setReservationEndDate(reservation.getReservationEndDate());
        entity.setCreationDate(LocalDateTime.now());
        entity.setModificationDate(LocalDateTime.now());
        entity.setPrice(reservation.getPrice());
        entity.setBooker(booker);
        entity.setRoom(room);

        entity = reservationRepository.saveAndFlush(entity);

        return entity.getId();
    }

    @Override
    public ReservationDto getReservationById(Long reservationId) throws ReservationNotFoundException {
        return reservationRepository.findById(reservationId)
                .map(reservationMapper::entityToDto)
                .orElseThrow(() -> new ReservationNotFoundException("The reservation " + reservationId + " does not exist !"));

    }

}
