package com.example.hotel;

import com.example.hotel.controller.UserDetails;
import com.example.hotel.dto.ReservationDto;
import com.example.hotel.dto.ReservationDtoList;
import com.example.hotel.dto.ReservationRequestDto;
import com.example.hotel.entity.CustomerEntity;
import com.example.hotel.entity.HotelEntity;
import com.example.hotel.entity.ReservationEntity;
import com.example.hotel.entity.RoomEntity;
import com.example.hotel.mapper.ReservationMapper;
import com.example.hotel.repository.CustomerRepository;
import com.example.hotel.repository.HotelRepository;
import com.example.hotel.repository.ReservationRepository;
import com.example.hotel.service.reservation.IReservationService;
import com.example.hotel.service.reservation.ReservationValidator;
import io.swagger.annotations.Authorization;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.*;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URL;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@EnableCaching
@ WithMockUser(username="admin",roles={"USER","ADMIN"})
public class ReservationIntegrationTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private IReservationService service;

    @Autowired
    private ReservationMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ReservationValidator validator;

    @Autowired
    private TestRestTemplate template;

    @LocalServerPort
    private int port;
    private URL base;

    private static final String BOOKER_FIRST_NAME = "camille";
    private static final String BOOKER_LAST_NAME = "guillard";
    private static final LocalDate BOOKER_DATE_OF_BIRTH = LocalDate.of(1993, 8, 20);
    private static final String HOTEL_CODE = "HOTEL001";
    private static final String ROOM_CODE = "ROOM001";
    private static final LocalDate RESERVATION_1_START_DATE = LocalDate.of(2020, 1, 1);
    private static final LocalDate RESERVATION_1_END_DATE = LocalDate.of(2020, 1, 7);
    private static final Double RESERVATION_1_PRICE = 100.00;
    private static final LocalDate RESERVATION_2_START_DATE = LocalDate.of(2024, 1, 1);
    private static final LocalDate RESERVATION_2_END_DATE = LocalDate.of(2024, 2, 1);
    private static final Double RESERVATION_2_PRICE = 500.00;


    @BeforeEach
    public void setUp()throws Exception {
        this.base = new URL("http://localhost:" + port);

        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(new UserDetails(1L, "camille", "guillard"));
        SecurityContextHolder.setContext(securityContext);

        this.initData();
    }

    @Test
    void getReservations() {
        ResponseEntity<ReservationDtoList> response = template.getForEntity(base.toString() + "/api/reservations", ReservationDtoList.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getReservations().size());

        ReservationDto reservationDtoResponse = response.getBody().getReservations().getFirst();
        assertEquals(BOOKER_FIRST_NAME, reservationDtoResponse.getBooker().getFirstName());
        assertEquals(BOOKER_LAST_NAME, reservationDtoResponse.getBooker().getLastName());
        assertEquals(BOOKER_DATE_OF_BIRTH, reservationDtoResponse.getBooker().getDateOfBirth());
        assertEquals(ROOM_CODE, reservationDtoResponse.getRoom().getCode());
        assertEquals(HOTEL_CODE, reservationDtoResponse.getRoom().getHotel().getCode());
        assertEquals(RESERVATION_1_START_DATE, reservationDtoResponse.getReservationStartDate());
        assertEquals(RESERVATION_1_END_DATE, reservationDtoResponse.getReservationEndDate());
        assertEquals(RESERVATION_1_PRICE, reservationDtoResponse.getPrice());
        assertNotNull(reservationDtoResponse.getCreationDate());
        assertNotNull(reservationDtoResponse.getModificationDate());
    }

    @Test
    void createReservationAndGetById() {
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto();
        reservationRequestDto.setBookerId(1L);
        reservationRequestDto.setRoomId(1L);
        reservationRequestDto.setReservationStartDate(RESERVATION_1_START_DATE);
        reservationRequestDto.setReservationEndDate(RESERVATION_1_END_DATE);
        reservationRequestDto.setCustomerIds(List.of(1L, 2L));
        reservationRequestDto.setPrice(RESERVATION_2_PRICE);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ReservationRequestDto> requestEntity = new HttpEntity<>(reservationRequestDto);

        HttpEntity<ReservationRequestDto> request = new HttpEntity<>(reservationRequestDto);
        ResponseEntity<Long> response = template.postForEntity(base.toString() + "/api/reservations/new", request, Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Long id = response.getBody();

        ResponseEntity<ReservationDto> response2 = template.getForEntity(base.toString() + "/api/reservations/" + id, ReservationDto.class);

        assertEquals(HttpStatus.OK, response2.getStatusCode());

        ReservationDto reservationDtoResponse = response2.getBody();
        assertEquals(reservationRequestDto.getBookerId(), reservationDtoResponse.getBooker().getId());
        assertEquals(BOOKER_FIRST_NAME, reservationDtoResponse.getBooker().getFirstName());
        assertEquals(BOOKER_LAST_NAME, reservationDtoResponse.getBooker().getLastName());
        assertEquals(BOOKER_DATE_OF_BIRTH, reservationDtoResponse.getBooker().getDateOfBirth());
        assertEquals(ROOM_CODE, reservationDtoResponse.getRoom().getCode());
        assertEquals(HOTEL_CODE, reservationDtoResponse.getRoom().getHotel().getCode());
        assertEquals(reservationRequestDto.getReservationStartDate(), reservationDtoResponse.getReservationStartDate());
        assertEquals(reservationRequestDto.getReservationEndDate(), reservationDtoResponse.getReservationEndDate());
        assertEquals(reservationRequestDto.getPrice(), reservationDtoResponse.getPrice());
        assertNotNull(reservationDtoResponse.getCreationDate());
        assertNotNull(reservationDtoResponse.getModificationDate());
    }

    private void initData() {
        HotelEntity hotel1 = new HotelEntity();
        hotel1.setCode(HOTEL_CODE);

        RoomEntity room1 = new RoomEntity();
        room1.setHotel(hotel1);
        room1.setCode(ROOM_CODE);
        hotel1.setRooms(List.of(room1));

        hotelRepository.saveAndFlush(hotel1);

        CustomerEntity customer = new CustomerEntity();
        customer.setFirstName(BOOKER_FIRST_NAME);
        customer.setLastName(BOOKER_LAST_NAME);
        customer.setDateOfBirth(BOOKER_DATE_OF_BIRTH);

        customerRepository.saveAndFlush(customer);

        ReservationEntity r1 = new ReservationEntity();
        r1.setCreationDate(LocalDateTime.now());
        r1.setModificationDate(LocalDateTime.now());
        r1.setReservationStartDate(RESERVATION_1_START_DATE);
        r1.setReservationEndDate(RESERVATION_1_END_DATE);
        r1.setBooker(customer);
        r1.setRoom(room1);
        r1.setPrice(RESERVATION_1_PRICE);

        reservationRepository.saveAndFlush(r1);

    }


}
