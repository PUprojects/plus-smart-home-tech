package ru.yandex.practicum.commerce.order.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.commerce.contract.order.OrderOperations;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.order.service.OrderService;
import ru.yandex.practicum.commerce.request.order.ProductReturnRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class OrderController implements OrderOperations {
    public final OrderService orderService;

    @Override
    public List<OrderDto> getOrders(String username) {
        log.info("Запрос списка заказов пользователя {}", username);
        return orderService.getOrders(username);
    }

    @Override
    public OrderDto createOrder(String username, OrderDto newOrder) {
        log.info("Создание заказа {}", newOrder);
        return orderService.createOrder(username, newOrder);
    }

    @Override
    public OrderDto returnProducts(ProductReturnRequest request) {
        log.info("Возврат продуктов {}", request);
        return null;
    }

    @Override
    public OrderDto payment(UUID orderId) {
        log.info("Платёж по заказу {}", orderId);
        return null;
    }

    @Override
    public OrderDto paymentFailed(UUID orderId) {
        log.info("Ошибка платежа по заказу {}", orderId);
        return null;
    }

    @Override
    public OrderDto delivery(UUID orderId) {
        log.info("Доставка заказа {}", orderId);
        return null;
    }

    @Override
    public OrderDto deliveryFailed(UUID orderId) {
        log.info("Ошибка доставки заказа {}", orderId);
        return null;
    }

    @Override
    public OrderDto completed(UUID orderId) {
        log.info("Заказ {} завершён", orderId);
        return null;
    }

    @Override
    public OrderDto calculateTotal(UUID orderId) {
        log.info("Расчёт стоимости заказа {}", orderId);
        return null;
    }

    @Override
    public OrderDto calculateDelivery(UUID orderId) {
        log.info("Расчёт стоимости доставки заказа {}", orderId);
        return null;
    }

    @Override
    public OrderDto assembly(UUID orderId) {
        log.info("Сборка заказа {}", orderId);
        return null;
    }

    @Override
    public OrderDto assemblyFailed(UUID orderId) {
        log.info("Ошибка сборки заказа {}", orderId);
        return null;
    }

    @Override
    public Map<UUID, Long> getProducts(UUID orderId) {
        log.info("Запрос перечня продуктов заказа {}", orderId);
        return Map.of();
    }
}
