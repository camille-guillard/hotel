package com.example.hotel.repository;


import com.example.hotel.entity.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HotelRepository extends JpaRepository<HotelEntity, Long> {


}
