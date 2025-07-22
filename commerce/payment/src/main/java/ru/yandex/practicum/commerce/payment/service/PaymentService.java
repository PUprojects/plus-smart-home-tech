package ru.yandex.practicum.commerce.payment.service;

import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    PaymentDto doPayment (OrderDto order);

    BigDecimal getTotalCoast(OrderDto order);

    void refoundPayment(UUID paymentId);

    BigDecimal getProductsCoast(OrderDto order);

    void failedPayment(UUID paymentId);
}
