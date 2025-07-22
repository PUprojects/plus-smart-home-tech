package ru.yandex.practicum.commerce.request.order;

import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;

public record CreateNewOrderRequest(ShoppingCartDto shoppingCart, AddressDto deliveryAddress) {
}
