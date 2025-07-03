package ru.yandex.practicum.commerce.contract.shopping.store;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductDto;

import java.util.UUID;

public interface ShoppingStoreOperations {

    @GetMapping
    Page<ProductDto> getProducts(String category, @PageableDefault(sort = {"productName"}) Pageable pageable);

    @PutMapping
    ProductDto createNewProduct(@RequestBody ProductDto newProduct);

    @PostMapping
    ProductDto updateProduct(@RequestBody ProductDto updatedProduct);

    @PostMapping("/removeProductFromStore")
    boolean removeProductFromStore(@RequestBody UUID productId);

    @PostMapping("/quantityState")
    boolean setProductQuantityState(@RequestParam UUID productId, @RequestParam String quantityState);

    @GetMapping("/{productId}")
    ProductDto getProduct(@PathVariable UUID productId);
}
