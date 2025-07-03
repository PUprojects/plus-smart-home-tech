package ru.yandex.practicum.commerce.shopping.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.contract.shopping.store.ShoppingStoreOperations;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductDto;
import ru.yandex.practicum.commerce.dto.shopping.store.QuantityState;
import ru.yandex.practicum.commerce.request.shopping.store.SetProductQuantityStateRequest;
import ru.yandex.practicum.commerce.shopping.store.service.ShoppingStoreService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
@Slf4j
public class ShoppingStoreController implements ShoppingStoreOperations {
    private final ShoppingStoreService shoppingStoreService;
    private final ObjectMapper tmpMapper = new ObjectMapper();

    @Override
    public Page<ProductDto> getProducts(String category, Pageable pageable) {
        log.info("Запрос продуктов категории {}, страница {}", category, pageable);
        Page<ProductDto> result = shoppingStoreService.getProducts(category, pageable);
        try {
            //            log.info("Sort direction: {}", result.getPageable().getSort().getOrderFor("productName").getDirection());
            log.info("Result is {}", tmpMapper.writeValueAsString(result));
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    @ResponseStatus(value = HttpStatus.CREATED)
    public ProductDto createNewProduct(ProductDto newProduct) {
        log.info("Запрос создания нового продукта {}", newProduct);
        return shoppingStoreService.createNewProduct(newProduct);
    }

    @Override
    public ProductDto updateProduct(ProductDto updatedProduct) {
        log.info("Запрос на изменение продукта {}", updatedProduct);
        return shoppingStoreService.updateProduct(updatedProduct);
    }

   @Override
   public boolean removeProductFromStore(UUID productId) {
        log.info("Запрос на удаление продукта {}", productId);
        return shoppingStoreService.removeProductFromStore(productId);
    }

    @Override
    public boolean setProductQuantityState(@PathVariable UUID productId, @PathVariable String quantityState)
    {
        SetProductQuantityStateRequest request = new SetProductQuantityStateRequest(productId,
                QuantityState.valueOf(quantityState));
        log.info("Запрос изменения корличества продукта {}", request);
        return shoppingStoreService.setProductQuantityState(request);
    }

    @Override
    public ProductDto getProduct(UUID productId) {
        log.info("Запрос продукта {}", productId);
        return shoppingStoreService.getProduct(productId);
    }
}
