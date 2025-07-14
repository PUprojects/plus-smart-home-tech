package ru.yandex.practicum.commerce.dto.shopping.cart;

import java.util.Map;
import java.util.UUID;

public record ShoppingCartDto(UUID shoppingCartId, Map<UUID, Long> products) {
}
