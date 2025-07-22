package ru.yandex.practicum.commerce.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.contract.warehouse.WarehouseOperations;

@FeignClient(name = "warehouse")
public interface WarehouseClient extends WarehouseOperations {
}
