package ru.yandex.practicum.commerce.warehouse.service;

import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.commerce.request.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.request.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.request.warehouse.NewProductInWarehouseRequest;

import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    void addProduct(NewProductInWarehouseRequest request);

    BookedProductsDto checkProductCount(Map<UUID, Long> products);

    void addProductQuantity(AddProductToWarehouseRequest request);

    AddressDto getWarehouseAddress();

    void returnProductsToWarehouse(Map<UUID, Long> products);

    BookedProductsDto assemblyOrder(AssemblyProductsForOrderRequest request);

    void shipToDelivery(ShippedToDeliveryRequest request);
}
