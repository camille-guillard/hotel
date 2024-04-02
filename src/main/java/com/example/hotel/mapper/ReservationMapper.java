package com.example.hotel.mapper;

import com.example.hotel.dto.ReservationDto;
import com.example.hotel.entity.ReservationEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = {HotelMapper.class, RoomMapper.class, CustomerMapper.class})
public interface ReservationMapper {

    List<ReservationDto> entityListToDto(List<ReservationEntity> reservationEntity);

    ReservationDto entityToDto(ReservationEntity reservationEntity);

    ReservationEntity dtoToEntity(ReservationDto reservationDto);

}
