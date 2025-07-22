package ru.yandex.practicum.commerce.delivery.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.commerce.delivery.model.DeliveryEntity;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeliveryMapper {
    DeliveryDto toDto(DeliveryEntity delivery);
}
