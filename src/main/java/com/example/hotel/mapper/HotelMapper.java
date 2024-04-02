package com.example.hotel.mapper;

import com.example.hotel.dto.HotelDto;
import com.example.hotel.entity.HotelEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface HotelMapper {

    List<HotelDto> entityListToDto(List<HotelEntity> hotelEntity);

    HotelDto entityToDto(HotelEntity hotelEntity);

}
