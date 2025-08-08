package ru.yandex.practicum.commerce.request.warehouse;

import java.util.Map;
import java.util.UUID;

public record AssemblyProductsForOrderRequest(Map<UUID, Long> products, UUID orderId) {
}
