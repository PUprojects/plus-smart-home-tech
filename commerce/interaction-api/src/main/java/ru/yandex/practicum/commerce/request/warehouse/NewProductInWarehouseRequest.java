package ru.yandex.practicum.commerce.request.warehouse;

import ru.yandex.practicum.commerce.dto.warehouse.DimensionDto;

import java.util.UUID;

public record NewProductInWarehouseRequest(UUID productId, Boolean fragile, DimensionDto dimension, Double weight) {
}
