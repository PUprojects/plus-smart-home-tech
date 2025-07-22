package ru.yandex.practicum.commerce.delivery.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.commerce.delivery.model.DeliveryAddress;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AddressMapper {
    AddressDto toDto(DeliveryAddress address);
}
