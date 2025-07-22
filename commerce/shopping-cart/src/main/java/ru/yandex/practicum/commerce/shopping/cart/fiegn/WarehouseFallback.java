package ru.yandex.practicum.commerce.shopping.cart.fiegn;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.commerce.request.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.request.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.request.warehouse.NewProductInWarehouseRequest;

import java.util.Map;
import java.util.UUID;

@Slf4j
public class WarehouseFallback implements WarehouseClient {
    @Override
    public void addProduct(NewProductInWarehouseRequest request) {
        log.info("Вызов заглушки addProduct с параметрами {}", request);
    }

    @Override
    public BookedProductsDto checkProductCount(ShoppingCartDto shoppingCartDto) {
        BookedProductsDto bookedProductsDto = new BookedProductsDto(0.0, 0.0, false);
        log.info("Вызов заглушки BookedProductsDto с параметрами {}", shoppingCartDto);
        return bookedProductsDto;
    }

    @Override
    public void addProductQuantity(AddProductToWarehouseRequest request) {
        log.info("Вызов заглушки addProductQuantity с параметрами {}", request);
    }

    @Override
    public AddressDto getWarehouseAddress() {
        log.info("Вызов заглушки getWarehouseAddress");
        return new AddressDto("NOT_FOUND", "NOT_FOUND", "NOT_FOUND", "NOT_FOUND", "NOT_FOUND");
    }

    @Override
    public void shipToDelivery(ShippedToDeliveryRequest request) {
        log.info("Вызов заглушки shipToDelivery");
    }

    @Override
    public void returnToWarehouse(Map<UUID, Long> products) {
        log.info("Вызов заглушки returnToWarehouse");
    }

    @Override
    public BookedProductsDto assemblyOrder(AssemblyProductsForOrderRequest request) {
        log.info("Вызов заглушки assemblyOrder");
        return new BookedProductsDto(0.0, 0.0, false);
    }
}
