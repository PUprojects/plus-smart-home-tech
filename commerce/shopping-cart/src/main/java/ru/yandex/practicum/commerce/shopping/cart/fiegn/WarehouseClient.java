package ru.yandex.practicum.commerce.shopping.cart.fiegn;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.commerce.contract.warehouse.WarehouseOperations;

@FeignClient(name = "warehouse", fallback = WarehouseFallback.class)
public interface WarehouseClient extends WarehouseOperations{
}
