package com.example.hotel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "T_ROOM")
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ROOM_ID")
    private Long id;

    @Column(name = "ROOM_CODE")
    private String code;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "HOTEL_ID")
    private HotelEntity hotel;

}
