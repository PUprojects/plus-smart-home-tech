package ru.yandex.practicum.commerce.warehouse.service.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.commerce.request.warehouse.NewProductInWarehouseRequest;
import ru.yandex.practicum.commerce.warehouse.model.WarehouseProduct;

@Component
public class WarehouseProductMapper {
    public WarehouseProduct toWarehouseProduct(NewProductInWarehouseRequest request) {
        WarehouseProduct product = new WarehouseProduct();

        product.setProductId(request.productId());
        product.setWeight(request.weight());
        product.setHeight(request.dimension().height());
        product.setWidth(request.dimension().width());
        product.setDepth(request.dimension().depth());
        product.setFragile(request.fragile());
        product.setQuantity(0L);

        return product;
    }
}
