package ru.yandex.practicum.commerce.contract.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.request.order.ProductReturnRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Validated
public interface OrderOperations {
    @GetMapping("/api/v1/order")
    List<OrderDto> getOrders (@Valid @RequestParam @NotEmpty String username);

    @PutMapping("/api/v1/order")
    OrderDto createOrder(@Valid @RequestParam @NotEmpty String username, @RequestBody OrderDto newOrder);

    @PostMapping("/api/v1/order/return")
    OrderDto returnProducts(@Valid @RequestBody ProductReturnRequest request);

    @PostMapping("/api/v1/order/payment")
    OrderDto payment(@Valid @RequestBody UUID orderId);

    @PostMapping("/api/v1/order/payment/failed")
    OrderDto paymentFailed(@Valid @RequestBody UUID orderId);

    @PostMapping("/api/v1/order/delivery")
    OrderDto delivery(@Valid @RequestBody UUID orderId);

    @PostMapping("/api/v1/order/delivery/failed")
    OrderDto deliveryFailed(@Valid @RequestBody UUID orderId);

    @PostMapping("/api/v1/order/completed")
    OrderDto completed(@Valid @RequestBody UUID orderId);

    @PostMapping("/api/v1/order/calculate/total")
    OrderDto calculateTotal(@Valid @RequestBody UUID orderId);

    @PostMapping("/api/v1/order/calculate/delivery")
    OrderDto calculateDelivery(@Valid @RequestBody UUID orderId);

    @PostMapping("/api/v1/order/assembly")
    OrderDto assembly(@Valid @RequestBody UUID orderId);

    @PostMapping("/api/v1/order/assembly/failed")
    OrderDto assemblyFailed(@Valid @RequestBody UUID orderId);

    @GetMapping("/api/v1/order/products")
    Map<UUID, Long> getProducts(@Valid @RequestBody UUID orderId);
}
