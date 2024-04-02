package com.example.hotel.mapper;

import com.example.hotel.dto.CustomerDto;
import com.example.hotel.entity.CustomerEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface CustomerMapper {

    List<CustomerDto> entityListToDto(List<CustomerEntity> customerEntity);

    CustomerDto entityToDto(CustomerEntity customerEntity);

}
