package ru.yandex.practicum.commerce.dto.shopping.store;


import java.util.UUID;

public record ProductDto (UUID productId, String productName, String description, String imageSrc,
                          QuantityState quantityState, ProductState productState, ProductCategory productCategory, float price) {
}
