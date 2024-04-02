package com.example.hotel;

import com.example.hotel.controller.ReservationControllerTest;
import com.example.hotel.repository.ReservationRepositoryTest;
import com.example.hotel.service.ReservationServiceTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ReservationControllerTest.class,
        ReservationServiceTest.class,
        ReservationRepositoryTest.class,
        ReservationIntegrationTest.class})

public class ReservationServiceTestSuite {
}
