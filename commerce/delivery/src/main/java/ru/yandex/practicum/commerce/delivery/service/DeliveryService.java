package ru.yandex.practicum.commerce.delivery.service;

import ru.yandex.practicum.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {
    DeliveryDto addDelivery(DeliveryDto newDelivery);

    void successfulDelivery(UUID deliveryId);

    void pickedDelivery(UUID deliveryId);

    void filedDelivery(UUID deliveryId);

    BigDecimal getDeliveryCoast(OrderDto order);
}
