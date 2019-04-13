package com.burgerapp.mapper;

import com.burgerapp.domain.OrderData;
import com.burgerapp.model.OrderDataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderDataMapper {

    OrderDataMapper INSTANCE = Mappers.getMapper(OrderDataMapper.class);

    OrderDataDTO toOrderDataDTO(OrderData orderData);

    OrderData toOrderData(OrderDataDTO dto);
}
