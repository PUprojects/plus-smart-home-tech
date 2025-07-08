package ru.yandex.practicum.commerce.shopping.cart.service;

import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.request.shopping.cart.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CartService {
    ShoppingCartDto getShoppingCart(String username);

    ShoppingCartDto addToCart(Map<UUID, Long> products, String username);

    void deleteCart(String username);

    ShoppingCartDto removeFromCart(List<UUID> products, String username);

    ShoppingCartDto changeProductQuantity(ChangeProductQuantityRequest request, String username);
}
