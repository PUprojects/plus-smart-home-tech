package ru.yandex.practicum.commerce.dto.shopping.store;


import java.math.BigDecimal;
import java.util.UUID;

public record ProductDto (UUID productId, String productName, String description, String imageSrc,
                          QuantityState quantityState, ProductState productState, ProductCategory productCategory, BigDecimal price) {
}
