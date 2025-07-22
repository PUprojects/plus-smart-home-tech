package ru.yandex.practicum.commerce.dto.order;

import ru.yandex.practicum.commerce.dto.shopping.cart.OrderState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public record OrderDto(UUID orderId, UUID shoppingCartId, Map<UUID, Long> products, UUID paymentId,
                       UUID deliveryId, OrderState state, Double deliveryWeight, Double deliveryVolume,
                       Boolean fragile, BigDecimal totalPrice, BigDecimal deliveryPrice, BigDecimal productPrice) {
}
