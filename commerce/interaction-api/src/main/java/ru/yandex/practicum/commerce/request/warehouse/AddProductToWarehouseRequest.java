package ru.yandex.practicum.commerce.request.warehouse;

import java.util.UUID;

public record AddProductToWarehouseRequest(UUID productId, Long quantity) {
}
