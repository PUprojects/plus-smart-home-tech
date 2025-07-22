package ru.yandex.practicum.commerce.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.payment.PaymentOperations;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.payment.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PaymentController implements PaymentOperations {
    private final PaymentService paymentService;

    @Override
    public PaymentDto doPayment(OrderDto order) {
        log.info("Запрос на создание платежа для заказа {}", order);
        return paymentService.doPayment(order);
    }

    @Override
    public BigDecimal getTotalCoast(OrderDto order) {
        log.info("Запрос полной стоимости заказа {}", order);
        return paymentService.getTotalCoast(order);
    }

    @Override
    public void refoundPayment(UUID paymentId) {
        log.info("Запрос установки успешного статуса заказа {}", paymentId);
        paymentService.refoundPayment(paymentId);
    }

    @Override
    public BigDecimal getProductsCoast(OrderDto order) {
        log.info("Запрос стоимости продуктов в заказе {}", order);
        return paymentService.getProductsCoast(order);
    }

    @Override
    public void failedPayment(UUID paymentId) {
        log.info("Запрос установки неудачного статуса оплаты для заказа {}", paymentId);
    }
}
