package ru.yandex.practicum.commerce.delivery.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.delivery.DeliveryOperations;
import ru.yandex.practicum.commerce.delivery.service.DeliveryService;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DeliveryController implements DeliveryOperations {
    DeliveryService deliveryService;

    @Override
    public DeliveryDto addDelivery(DeliveryDto newDelivery) {
        log.info("Запрос на создание доставки {}",newDelivery);
        return deliveryService.addDelivery(newDelivery);
    }

    @Override
    public void successfulDelivery(UUID deliveryId) {
        log.info("Успешная доставка {}", deliveryId);
        deliveryService.successfulDelivery(deliveryId);
    }

    @Override
    public void pickedDelivery(UUID deliveryId) {
        log.info("Принять товары на доставку {}", deliveryId);
        deliveryService.pickedDelivery(deliveryId);
    }

    @Override
    public void filedDelivery(UUID deliveryId) {
        log.info("Ошибка доставки {}", deliveryId);
        deliveryService.filedDelivery(deliveryId);
    }

    @Override
    public BigDecimal getDeliveryCoast(OrderDto order) {
        log.info("Запрос стоимости доставки заказа {}", order);
        return deliveryService.getDeliveryCoast(order);
    }
}
