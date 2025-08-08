package ru.yandex.practicum.commerce.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.contract.payment.PaymentOperations;

@FeignClient(name = "payment")
public interface PaymentClient extends PaymentOperations {
}
