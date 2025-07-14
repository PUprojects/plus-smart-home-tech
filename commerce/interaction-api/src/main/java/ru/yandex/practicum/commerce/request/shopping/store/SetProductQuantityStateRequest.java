package ru.yandex.practicum.commerce.request.shopping.store;

import ru.yandex.practicum.commerce.dto.shopping.store.QuantityState;

import java.util.UUID;

public record SetProductQuantityStateRequest (UUID productId, QuantityState quantityState) {
}
