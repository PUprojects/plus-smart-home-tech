package ru.yandex.practicum.commerce.shopping.store.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductDto;
import ru.yandex.practicum.commerce.request.shopping.store.SetProductQuantityStateRequest;

import java.util.UUID;

public interface ShoppingStoreService {
    Page<ProductDto> getProducts(String category, Pageable pageable);

    ProductDto createNewProduct(ProductDto newProduct);

    ProductDto updateProduct(ProductDto updatedProduct);

    boolean removeProductFromStore(UUID productId);

    boolean setProductQuantityState(SetProductQuantityStateRequest request);

    ProductDto getProduct(UUID productId);
}
