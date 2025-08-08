package ru.yandex.practicum.commerce.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.dto.warehouse.ShippedToDeliveryRequest;
import ru.yandex.practicum.commerce.exception.warehouse.NoSpecifiedOrderInWarehouse;
import ru.yandex.practicum.commerce.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.commerce.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.commerce.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.commerce.request.warehouse.AddProductToWarehouseRequest;
import ru.yandex.practicum.commerce.request.warehouse.AssemblyProductsForOrderRequest;
import ru.yandex.practicum.commerce.request.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.warehouse.model.BookedProducts;
import ru.yandex.practicum.commerce.warehouse.model.OrderBooking;
import ru.yandex.practicum.commerce.warehouse.model.WarehouseProduct;
import ru.yandex.practicum.commerce.warehouse.repository.WarehouseOrderRepository;
import ru.yandex.practicum.commerce.warehouse.repository.WarehouseProductRepository;
import ru.yandex.practicum.commerce.warehouse.service.mapper.WarehouseProductMapper;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseProductRepository warehouseProductRepository;
    private final WarehouseProductMapper warehouseProductMapper;
    private final WarehouseOrderRepository warehouseOrderRepository;

    private static final String[] ADDRESSES =
            new String[] {"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    @Override
    public void addProduct(NewProductInWarehouseRequest request) {
        warehouseProductRepository.findById(request.productId())
                .ifPresent(warehouseProduct -> {
                    throw new SpecifiedProductAlreadyInWarehouseException("Товар " + warehouseProduct.getProductId() +
                        " уже есть в базе склада");
                });

        WarehouseProduct product = warehouseProductMapper.toWarehouseProduct(request);
        warehouseProductRepository.save(product);
    }

    @Override
    public BookedProductsDto checkProductCount(Map<UUID, Long> products) {
        BookedProducts bookedProducts = new BookedProducts();
        products.forEach((productId, quantity) -> {
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
        warehouseProductRepository.save(warehouseProduct);
    }

    @Override
    public AddressDto getWarehouseAddress() {

        return new AddressDto(CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS, CURRENT_ADDRESS);
    }

    @Override
    public void returnProductsToWarehouse(Map<UUID, Long> products) {
        List<WarehouseProduct> warehouseProducts = warehouseProductRepository.findAllById(products.keySet());

        if(warehouseProducts.isEmpty()) {
            return;
        }

        warehouseProducts.forEach(warehouseProduct ->
                warehouseProduct.setQuantity(warehouseProduct.getQuantity() +
                products.get(warehouseProduct.getProductId())));

        warehouseProductRepository.saveAll(warehouseProducts);
    }

    @Override
    public BookedProductsDto assemblyOrder(AssemblyProductsForOrderRequest request) {
        BookedProductsDto result = checkProductCount(request.products());
        OrderBooking newBooking = new OrderBooking();
        newBooking.setOrderId(request.orderId());
        newBooking.setProducts(request.products());
        warehouseOrderRepository.save(newBooking);
        return result;
    }

    @Override
    public void shipToDelivery(ShippedToDeliveryRequest request) {
        OrderBooking order = warehouseOrderRepository.findById(request.orderId())
                .orElseThrow(() -> new NoSpecifiedOrderInWarehouse("Заказа " + request.orderId() +
                        " нет  базе склада"));

        order.setDeliveryId(request.deliveryId());
        warehouseOrderRepository.save(order);
    }

    private WarehouseProduct findWarehouseProductById(UUID productId) {
        return warehouseProductRepository.findById(productId)
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("Продукт " + productId +
                        " не найден на складе"));
    }
}
