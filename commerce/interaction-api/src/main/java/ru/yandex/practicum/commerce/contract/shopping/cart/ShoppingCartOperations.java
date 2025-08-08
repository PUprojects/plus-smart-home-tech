package ru.yandex.practicum.commerce.contract.shopping.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.shopping.cart.ShoppingCartDto;
import ru.yandex.practicum.commerce.request.shopping.cart.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Validated
public interface ShoppingCartOperations {
    @GetMapping("/api/v1/shopping-cart")
    ShoppingCartDto getShoppingCart(@Valid  @RequestParam @NotEmpty String username);

    @PutMapping("/api/v1/shopping-cart")
    ShoppingCartDto addToCart(@Valid @RequestBody Map<UUID, Long> products,
                              @RequestParam @NotEmpty String username);

    @DeleteMapping("/api/v1/shopping-cart")
    void deleteCart(@Valid @RequestParam @NotEmpty String username);

    @PostMapping("/api/v1/shopping-cart/remove")
    ShoppingCartDto removeFromCart(@Valid @RequestBody List<UUID> products,
                                   @RequestParam @NotEmpty String username);

    @PostMapping("/api/v1/shopping-cart/change-quantity")
    ShoppingCartDto changeProductQuantity(@Valid @RequestBody ChangeProductQuantityRequest request,
                                          @RequestParam @NotEmpty String username);
}
