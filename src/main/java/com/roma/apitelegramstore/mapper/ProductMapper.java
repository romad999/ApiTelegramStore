package com.roma.apitelegramstore.mapper;

import com.roma.apitelegramstore.dto.ProductRequestDto;
import com.roma.apitelegramstore.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    Product toEntity(ProductRequestDto dto);
}
