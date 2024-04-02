package com.example.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerDto {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if( ! (obj instanceof CustomerDto) ) return false;

        CustomerDto other = (CustomerDto) obj;

        return this.id !=null ? this.id.equals(other.id) : this.id == other.id;
    }

    @Override
    public int hashCode() {
        return firstName == null? 0 : firstName.length() + lastName.length();
    }

}
