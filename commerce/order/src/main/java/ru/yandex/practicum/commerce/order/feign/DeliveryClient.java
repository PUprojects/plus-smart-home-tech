package ru.yandex.practicum.commerce.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.contract.delivery.DeliveryOperations;

@FeignClient(name = "delivery")
public interface DeliveryClient extends DeliveryOperations {
}
