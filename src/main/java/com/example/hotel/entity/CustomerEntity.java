package com.example.hotel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "T_CUSTOMER")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CUSTOMER_ID")
    private Long id;

    @Column(name = "CUSTOMER_FIRST_NAME")
    private String firstName;

    @Column(name = "CUSTOMER_LAST_NAME")
    private String lastName;

    @Column(name = "CUSTOMER_DATE_OF_BIRTH")
    private LocalDate dateOfBirth;

}
