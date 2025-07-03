package ru.yandex.practicum.commerce.request.shopping.cart;

import java.util.UUID;

public record ChangeProductQuantityRequest(UUID productId, Long newQuantity) {
}
