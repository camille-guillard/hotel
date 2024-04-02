package com.example.hotel.repository;

import com.example.hotel.TestConfig;
import com.example.hotel.entity.CustomerEntity;
import com.example.hotel.entity.HotelEntity;
import com.example.hotel.entity.ReservationEntity;
import com.example.hotel.entity.RoomEntity;
import jakarta.transaction.Transactional;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.util.AssertionErrors.*;

@DataJpaTest
@ContextConfiguration(classes= TestConfig.class)
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository repository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private CustomerRepository customerRepository;


    @BeforeEach
    public void init() {
        this.mockData();
    }


    @Test
    public void testFindAll() {
        assertEquals("Should return 3 reservations",
                3,
                repository.findAll().size());
    }

    @Test
    public void testFindByStartDateAndEndDate1() {
        assertEquals("Should return the reservation with id : 1",
                1,
                repository.findByStartDateAndEndDate(
                        LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 2, 2)
                ).size());
    }

    @Test
    public void testFindByStartDateAndEndDate2() {
        assertEquals("Should return the reservation with id : 1",
                1,
                repository.findByStartDateAndEndDate(
                        LocalDate.of(2024, 1, 2),
                        LocalDate.of(2024, 10, 2)
                ).size());
    }

    @Test
    public void testFindByStartDateAndEndDate3() {
        assertEquals("Should return the reservation with id : 1",
                1,
                repository.findByStartDateAndEndDate(
                        LocalDate.of(2024, 2, 3),
                        LocalDate.of(2024, 2, 4)
                ).size());
    }

    @Test
    public void testFindByStartDateAndEndDate4() {
        assertEquals("Should return the reservation with id : 1",
                1,
                repository.findByStartDateAndEndDate(
                        LocalDate.of(2024, 2, 3),
                        null
                ).size());
    }

    @Test
    public void testFindByStartDateAndEndDate5() {
        assertEquals("Should return the reservation with id : 1",
                1,
                repository.findByStartDateAndEndDate(
                        null,
                        LocalDate.of(2024, 2, 3)
                ).size());
    }

    @Test
    public void testFindByStartDateAndEndDate6() {
        assertEquals("Should return the reservation with id : 1",
                0,
                repository.findByStartDateAndEndDate(
                        LocalDate.of(2025, 2, 3),
                        LocalDate.of(2025, 2, 3)
                ).size());
    }

    @Test
    public void testFindByStartDateAndEndDate7() {
        assertEquals("Should return the reservation with id : 1",
                3,
                repository.findByStartDateAndEndDate(
                        LocalDate.of(1970, 1, 1),
                        LocalDate.of(2070, 1, 1)
                ).size());
    }

    @Test
    public void testFindByStartDateAndEndDate8() {
        assertEquals("Should return the reservation with id : 1",
                3,
                repository.findByStartDateAndEndDate(null, null).size());
    }

    @Test
    public void testFindReservationAveragePriceByHotelId() {

        Double average = repository.findReservationAveragePriceByHotelCode("HOTEL001");

        assertEquals("Should return the average price of the reservations from the hotel HOTEL001",
                550.00, average);

    }

    private void mockData() {
        HotelEntity hotel1 = new HotelEntity();
        hotel1.setCode("HOTEL001");

        RoomEntity room1 = new RoomEntity();
        room1.setHotel(hotel1);
        hotel1.setRooms(List.of(room1));

        hotelRepository.saveAndFlush(hotel1);

        CustomerEntity customer = new CustomerEntity();
        customer.setFirstName("camille");
        customer.setLastName("guillard");
        customer.setDateOfBirth(LocalDate.of(1993, 8, 20));

        customerRepository.saveAndFlush(customer);

        ReservationEntity r1 = new ReservationEntity();
        r1.setCreationDate(LocalDateTime.now());
        r1.setModificationDate(LocalDateTime.now());
        r1.setReservationStartDate(LocalDate.of(2024, 2, 1));
        r1.setReservationEndDate(LocalDate.of(2024, 2, 10));
        r1.setBooker(customer);
        r1.setRoom(room1);
        r1.setPrice(100.00);

        repository.saveAndFlush(r1);

        ReservationEntity r2 = new ReservationEntity();
        r2.setCreationDate(LocalDateTime.now());
        r2.setModificationDate(LocalDateTime.now());
        r2.setReservationStartDate(LocalDate.of(2023, 2, 1));
        r2.setReservationEndDate(LocalDate.of(2023, 2, 10));
        r2.setBooker(customer);
        r2.setRoom(room1);
        r2.setPrice(1000.00);

        repository.saveAndFlush(r2);

        HotelEntity hotel2 = new HotelEntity();
        hotel2.setCode("HOTEL002");

        RoomEntity room2 = new RoomEntity();
        room2.setHotel(hotel2);
        hotel2.setRooms(List.of(room2));

        hotelRepository.saveAndFlush(hotel2);

        CustomerEntity customer2 = new CustomerEntity();
        customer.setFirstName("camille2");
        customer.setLastName("guillard2");
        customer.setDateOfBirth(LocalDate.of(1993, 8, 20));

        customerRepository.saveAndFlush(customer2);

        ReservationEntity r3 = new ReservationEntity();
        r3.setCreationDate(LocalDateTime.now());
        r3.setModificationDate(LocalDateTime.now());
        r3.setReservationStartDate(LocalDate.of(2023, 2, 1));
        r3.setReservationEndDate(LocalDate.of(2023, 2, 10));
        r3.setBooker(customer2);
        r3.setPrice(300.00);
        r3.setRoom(room2);

        repository.saveAndFlush(r3);
    }
}
