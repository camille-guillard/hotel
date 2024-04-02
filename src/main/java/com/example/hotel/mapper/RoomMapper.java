package com.example.hotel.mapper;

import com.example.hotel.dto.RoomDto;
import com.example.hotel.entity.RoomEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = HotelMapper.class)
public interface RoomMapper {

    List<RoomDto> entityListToDto(List<RoomEntity> roomEntity);

    RoomDto entityToDto(RoomEntity roomEntity);

}
