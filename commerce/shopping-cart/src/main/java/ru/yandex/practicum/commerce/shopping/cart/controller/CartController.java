package ru.yandex.practicum.commerce.shopping.cart.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.shopping.cart.ShoppingCartOperations;
import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.request.shopping.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.shopping.cart.service.CartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CartController implements ShoppingCartOperations {
    private final CartService cartService;

    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        log.info("Поступил запрос корзины для пользователя {}", username);
        return cartService.getShoppingCart(username);
    }

    @Override
    public ShoppingCartDto addToCart(Map<UUID, Long> products, String username) {
        log.info("Для пользователя {} запрос на добавление продуктов", username);
        return cartService.addToCart(products, username);
    }

    @Override
    public void deleteCart(String username) {
        log.info("Запрос удаления корзины пользоветеля {}", username);
        cartService.deleteCart(username);
    }

    @Override
    public ShoppingCartDto removeFromCart(List<UUID> products, String username) {
        log.info("Запрос удаления товаров из корзины пользователя {}", username);
        return cartService.removeFromCart(products, username);
    }

    @Override
    public ShoppingCartDto changeProductQuantity(ChangeProductQuantityRequest request, String username) {
        log.info("Запрос изменения количества продукта {} на {} пользователя {}",
                request.productId(), request.newQuantity(), username);
        return cartService.changeProductQuantity(request, username);
    }
}
