package ru.yandex.practicum.commerce.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.dto.shopping.cart.OrderState;
import ru.yandex.practicum.commerce.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.commerce.exception.order.NoOrderFoundException;
import ru.yandex.practicum.commerce.order.feign.DeliveryClient;
import ru.yandex.practicum.commerce.order.feign.PaymentClient;
import ru.yandex.practicum.commerce.order.feign.WarehouseClient;
import ru.yandex.practicum.commerce.order.model.OrderEntity;
import ru.yandex.practicum.commerce.order.repository.OrderRepository;
import ru.yandex.practicum.commerce.order.service.mapper.OrderMapper;
import ru.yandex.practicum.commerce.request.order.ProductReturnRequest;
import ru.yandex.practicum.commerce.request.warehouse.AssemblyProductsForOrderRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final WarehouseClient warehouseClient;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;

    @Override
    public List<OrderDto> getOrders(String username) {
        return orderRepository.findAllByUsername(username).stream()
                .map(order -> orderMapper.toDto(order))
                .toList();
    }

    @Override
    public OrderDto createOrder(String username, OrderDto newOrder) {
        OrderEntity order = orderMapper.toEntity(newOrder);
        order.setOrderId(null);
        order.setUsername(username);
        order.setState(OrderState.NEW);

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto returnProducts(ProductReturnRequest request) {
        OrderEntity order = findById(request.orderId());

        request.products().forEach((productId, quantity) -> {
                    long newQuantity = order.getProducts().get(productId) - quantity;
                    if (newQuantity <= 0) {
                        order.getProducts().remove(productId);
                    } else {
                        order.getProducts().put(productId, newQuantity);
                    }
                }
        );

        orderRepository.save(order);

        warehouseClient.returnToWarehouse(request.products());

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto payment(UUID orderId) {
        OrderEntity order = findById(orderId);

        order.setState(OrderState.ON_PAYMENT);

        OrderDto orderDto = orderMapper.toDto(order);

        PaymentDto paymentDto = paymentClient.doPayment(orderDto);

        order.setPaymentId(paymentDto.paymentId());
        order.setDeliveryPrice(paymentDto.deliveryTotal());
        order.setProductPrice(paymentDto.totalPayment());
        order.setTotalPrice(paymentDto.feeTotal());

        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto paymentFailed(UUID orderId) {
        OrderEntity order = findById(orderId);

        order.setState(OrderState.PAYMENT_FAILED);
        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto delivery(UUID orderId) {
        OrderEntity order = findById(orderId);
        order.setState(OrderState.DELIVERED);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto deliveryFailed(UUID orderId) {
        OrderEntity order = findById(orderId);
        order.setState(OrderState.DELIVERY_FAILED);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto completed(UUID orderId) {
        OrderEntity order = findById(orderId);
        order.setState(OrderState.COMPLETED);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto calculateTotal(UUID orderId) {
        OrderEntity order = findById(orderId);
        BigDecimal productsCoast =  paymentClient.getProductsCoast(orderMapper.toDto(order));
        order.setProductPrice(productsCoast);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto calculateDelivery(UUID orderId) {
        OrderEntity order = findById(orderId);
        BigDecimal deliveryCoast = deliveryClient.getDeliveryCoast(orderMapper.toDto(order));
        order.setDeliveryPrice(deliveryCoast);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto assembly(UUID orderId) {
        OrderEntity order = findById(orderId);

        AssemblyProductsForOrderRequest request = new AssemblyProductsForOrderRequest(order.getProducts(), orderId);
        BookedProductsDto bookedProducts = warehouseClient.assemblyOrder(request);

        order.setDeliveryWeight(bookedProducts.deliveryWeight());
        order.setFragile(bookedProducts.fragile());
        order.setDeliveryVolume(bookedProducts.deliveryVolume());
        order.setState(OrderState.ASSEMBLED);

        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto assemblyFailed(UUID orderId) {
        OrderEntity order = findById(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public Map<UUID, Long> getProducts(UUID orderId) {
        OrderEntity order = findById(orderId);
        return order.getProducts();
    }

    private OrderEntity findById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NoOrderFoundException("Заказ с id " + orderId + " не найден"));
    }
}
