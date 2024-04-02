package com.example.hotel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "T_HOTEL")
public class HotelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOTEL_ID")
    private Long id;

    @Column(name = "HOTEL_CODE")
    private String code;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "HOTEL_ID")
    private List<RoomEntity> rooms = new ArrayList<>();


}