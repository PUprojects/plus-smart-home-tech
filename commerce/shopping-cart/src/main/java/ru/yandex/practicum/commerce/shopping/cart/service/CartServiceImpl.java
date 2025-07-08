package ru.yandex.practicum.commerce.shopping.cart.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.exception.shopping.cart.NoProductsInShoppingCartException;
import ru.yandex.practicum.commerce.exception.shopping.cart.NotAuthorizedUserException;
import ru.yandex.practicum.commerce.request.shopping.cart.ChangeProductQuantityRequest;
import ru.yandex.practicum.commerce.shopping.cart.fiegn.WarehouseClient;
import ru.yandex.practicum.commerce.shopping.cart.model.Cart;
import ru.yandex.practicum.commerce.shopping.cart.repository.CartRepository;
import ru.yandex.practicum.commerce.shopping.cart.service.mapper.CartMapper;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final WarehouseClient warehouseClient;

    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        return cartMapper.toShoppingCartDto(getCartByUsername(username));
    }

    @Override
    public ShoppingCartDto addToCart(Map<UUID, Long> products, String username) {
        Cart cart = cartRepository.findByUsername(username)
                .orElseGet(() -> {
                   Cart newCart = new Cart();
                   newCart.setUsername(username);
                   return newCart;
                });
        products.forEach((productId, quantity) -> cart.getProducts().put(productId, quantity));
        warehouseClient.checkProductCount(cartMapper.toShoppingCartDto(cart));
        cartRepository.save(cart);
        return cartMapper.toShoppingCartDto(cart);
    }

    @Override
    public void deleteCart(String username) {
        Cart cart = getCartByUsername(username);
        cartRepository.delete(cart);
    }

    @Override
    public ShoppingCartDto removeFromCart(List<UUID> products, String username) {
        Cart cart = getCartByUsername(username);
        products.forEach((productId) -> cart.getProducts().remove(productId));
        cartRepository.save(cart);
        return cartMapper.toShoppingCartDto(cart);
    }

    @Override
    public ShoppingCartDto changeProductQuantity(ChangeProductQuantityRequest request, String username) {
        Cart cart = getCartByUsername(username);
        if(cart.getProducts().get(request.productId()) == null) {
            throw new NoProductsInShoppingCartException("Продукта " + request.productId() +
                    " нет в корзине");
        }

        cart.getProducts().put(request.productId(), request.newQuantity());

        ShoppingCartDto shoppingCartDto = cartMapper.toShoppingCartDto(cart);
        warehouseClient.checkProductCount(shoppingCartDto);
        cartRepository.save(cart);
        return shoppingCartDto;
    }

    private Cart getCartByUsername(String username) {
        return cartRepository.findByUsername(username)
                .orElseThrow(() -> new NotAuthorizedUserException("Пользователь " + username +
                        " не авторизован в системе и не имеет корзины"));
    }
}
