package ru.yandex.practicum.commerce.warehouse.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.warehouse.WarehouseOperations;
import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.commerce.request.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.request.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.request.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.warehouse.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class WarehouseController implements WarehouseOperations {
    private final WarehouseService warehouseService;
    @Override
    public void addProduct(NewProductInWarehouseRequest request) {
        log.info("Запрос на добавление карточки товара {}", request);
        warehouseService.addProduct(request);
    }

    @Override
    public BookedProductsDto checkProductCount(ShoppingCartDto shoppingCartDto) {
        log.info("Запрос на проверку корзины {}", shoppingCartDto);
        return warehouseService.checkProductCount(shoppingCartDto.products());
    }

    @Override
    public void addProductQuantity(AddProductToWarehouseRequest request) {
        log.info("Запрос на добавления товара на склад {}", request);
        warehouseService.addProductQuantity(request);
    }

    @Override
    public AddressDto getWarehouseAddress() {
        log.info("Запрос адреса склада");
        return warehouseService.getWarehouseAddress();
    }

    @Override
    public void shipToDelivery(ShippedToDeliveryRequest request) {
        log.info("Запрос на передачу в доставку {}", request);

    }

    @Override
    public void returnToWarehouse(Map<UUID, Long> products) {
        log.info("Запрос возврата на склад {}", products);
        warehouseService.returnProductsToWarehouse(products);
    }

    @Override
    public BookedProductsDto assemblyOrder(AssemblyProductsForOrderRequest request) {
        log.info("Запрос на сборку заказа {}", request);
        return warehouseService.assemblyOrder(request);
    }
}
