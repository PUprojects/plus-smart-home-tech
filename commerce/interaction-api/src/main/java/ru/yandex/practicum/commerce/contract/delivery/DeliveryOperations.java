package ru.yandex.practicum.commerce.contract.delivery;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.dto.order.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@Validated
public interface DeliveryOperations {
    @PutMapping("/api/v1/delivery")
    DeliveryDto addDelivery(@Valid @RequestBody DeliveryDto newDelivery);

    @PostMapping("/api/v1/delivery/successful")
    void successfulDelivery(@Valid @RequestBody UUID deliveryId);

    @PostMapping("/api/v1/delivery/picked")
    void pickedDelivery(@Valid @RequestBody UUID deliveryId);

    @PostMapping("/api/v1/delivery/failed")
    void filedDelivery(@Valid @RequestBody UUID deliveryId);

    @PostMapping("/api/v1/delivery/cost")
    BigDecimal getDeliveryCoast(@Valid @RequestBody OrderDto order);
}
