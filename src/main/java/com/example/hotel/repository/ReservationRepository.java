package com.example.hotel.repository;

import com.example.hotel.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    @Query("select r from ReservationEntity r " +
            "where (r.reservationStartDate between :startDate and :endDate) " +
            "or (:startDate is null and :endDate is null) " +
            "or (r.reservationEndDate between :startDate and :endDate) " +
            "or (:startDate between r.reservationStartDate and r.reservationEndDate) " +
            "or (:endDate between r.reservationStartDate and r.reservationEndDate) ")
    List<ReservationEntity> findByStartDateAndEndDate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);


    @Query("select round(avg(price), 2) from ReservationEntity r where r.room.hotel.code = :hotelCode group by r.room.hotel.code")
    Double findReservationAveragePriceByHotelCode(@Param("hotelCode") String hotelCode);


}
