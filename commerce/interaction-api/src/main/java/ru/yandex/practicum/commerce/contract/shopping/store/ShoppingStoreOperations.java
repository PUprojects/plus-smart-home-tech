package ru.yandex.practicum.commerce.contract.shopping.store;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductDto;

import java.util.UUID;

public interface ShoppingStoreOperations {

    @GetMapping("/api/v1/shopping-store")
    Page<ProductDto> getProducts(String category, @PageableDefault(sort = {"productName"}) Pageable pageable);

    @PutMapping("/api/v1/shopping-store")
    ProductDto createNewProduct(@RequestBody ProductDto newProduct);

    @PostMapping("/api/v1/shopping-store")
    ProductDto updateProduct(@RequestBody ProductDto updatedProduct);

    @PostMapping("/api/v1/shopping-store/removeProductFromStore")
    boolean removeProductFromStore(@RequestBody UUID productId);

    @PostMapping("/api/v1/shopping-store/quantityState")
    boolean setProductQuantityState(@RequestParam UUID productId, @RequestParam String quantityState);

    @GetMapping("/api/v1/shopping-store/{productId}")
    ProductDto getProduct(@PathVariable UUID productId);
}
