package com.example.hotel.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "T_RESERVATION")
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVATION_ID")
    private Long id;

    @Column(name = "RESERVATION_START_DATE")
    private LocalDate reservationStartDate;

    @Column(name = "RESERVATION_END_DATE")
    private LocalDate reservationEndDate;

    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name = "MODIFICATION_DATE")
    private LocalDateTime modificationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROOM_ID")
    private RoomEntity room;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "BOOKER_ID")
    private CustomerEntity booker;

    @Column(name = "RESERVATION_PRICE")
    private Double price;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable( name = "R_RESERVATION_CUSTOMER",
            joinColumns = @JoinColumn( name = "reservation_id" ),
            inverseJoinColumns = @JoinColumn( name = "customer_id" ))
    @OrderBy("CUSTOMER_LAST_NAME ASC")
    private List<CustomerEntity> customers = new ArrayList<>();

}
