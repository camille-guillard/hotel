package com.example.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomDto {
    private String code;
    private HotelDto hotel;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if( ! (obj instanceof RoomDto) ) return false;

        RoomDto other = (RoomDto) obj;

        return this.code !=null ? this.code.equals(other.code) : this.code == other.code;
    }

    @Override
    public int hashCode() {
        return code == null? 0 : code.length();
    }
}
