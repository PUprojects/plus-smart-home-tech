package ru.yandex.practicum.commerce.delivery.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.contract.order.OrderOperations;

@FeignClient(name = "order")
public interface OrderClient extends OrderOperations {
}
