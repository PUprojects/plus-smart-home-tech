package ru.yandex.practicum.commerce.contract.payment;

import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

@Validated
public interface PaymentOperations {
    @PostMapping("/api/v1/payment")
    PaymentDto doPayment(@Valid @RequestBody OrderDto order);

    @PostMapping("/api/v1/payment/totalCost")
    BigDecimal getTotalCoast(@Valid OrderDto order);

    @PostMapping("/api/v1/payment/refund")
    void refoundPayment(@Valid @RequestBody UUID paymentId);

    @PostMapping("/api/v1/payment/productCost")
    BigDecimal getProductsCoast(OrderDto order);

    @PostMapping("/api/v1/payment/failed")
    void failedPayment(UUID paymentId);
}
