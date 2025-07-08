package ru.yandex.practicum.commerce.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.commerce.request.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.request.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.warehouse.model.BookedProducts;
import ru.yandex.practicum.commerce.warehouse.model.WarehouseProduct;
import ru.yandex.practicum.commerce.warehouse.repository.WarehouseRepository;
import ru.yandex.practicum.commerce.warehouse.service.mapper.WarehouseProductMapper;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseProductMapper warehouseProductMapper;

    private static final String[] ADDRESSES =
            new String[] {"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    @Override
    public void addProduct(NewProductInWarehouseRequest request) {
        warehouseRepository.findById(request.productId())
                .ifPresent(warehouseProduct -> {
                    throw new SpecifiedProductAlreadyInWarehouseException("Товар " + warehouseProduct.getProductId() +
                        " уже есть в базе склада");
                });

        WarehouseProduct product = warehouseProductMapper.toWarehouseProduct(request);
        warehouseRepository.save(product);
    }

    @Override
    public BookedProductsDto checkProductCount(ShoppingCartDto shoppingCartDto) {
        BookedProducts bookedProducts = new BookedProducts();
        shoppingCartDto.products().forEach((productId, quantity) -> {
            WarehouseProduct warehouseProduct = findWarehouseProductById(productId);
            if(warehouseProduct.getQuantity() < quantity) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("На сладе недостаточно продукта " +
                        productId + ". Необходимо " + quantity + " есть" + warehouseProduct.getQuantity());
            }

            bookedProducts.setFragile(bookedProducts.getFragile() || warehouseProduct.getFragile());
            bookedProducts.setDeliveryWeight(bookedProducts.getDeliveryWeight() +
                    warehouseProduct.getWeight() * quantity);
            bookedProducts.setDeliveryVolume(bookedProducts.getDeliveryVolume() +
                    warehouseProduct.getWidth() * warehouseProduct.getHeight() *
                            warehouseProduct.getDepth() * quantity);
        });
        return new BookedProductsDto(bookedProducts.getDeliveryWeight(),
                bookedProducts.getDeliveryVolume(), bookedProducts.getFragile());
    }

    @Override
    public void addProductQuantity(AddProductToWarehouseRequest request) {
        WarehouseProduct warehouseProduct = findWarehouseProductById(request.productId());
        warehouseProduct.setQuantity(request.quantity());
        warehouseRepository.save(warehouseProduct);
    }

    @Override
    public AddressDto getWarehouseAddress() {

        return new AddressDto(CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS);
    }

    private WarehouseProduct findWarehouseProductById(UUID productId) {
        return warehouseRepository.findById(productId)
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Продукт " + productId +
                        " не найден на складе"));
    }
}
