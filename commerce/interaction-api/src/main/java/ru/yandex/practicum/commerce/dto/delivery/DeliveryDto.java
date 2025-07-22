package ru.yandex.practicum.commerce.dto.delivery;

import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;

import java.util.UUID;

public record DeliveryDto(UUID deliveryId, AddressDto fromAddress, AddressDto toAddress, UUID orderId,
                          DeliveryState deliveryState) {
}
