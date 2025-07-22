package ru.yandex.practicum.commerce.payment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.payment.PaymentDto;
import ru.yandex.practicum.commerce.dto.payment.PaymentStatus;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductDto;
import ru.yandex.practicum.commerce.exception.payment.NoOrderFoundException;
import ru.yandex.practicum.commerce.exception.payment.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.commerce.payment.feign.DeliveryClient;
import ru.yandex.practicum.commerce.payment.feign.ShoppingStoreClient;
import ru.yandex.practicum.commerce.payment.model.PaymentEntity;
import ru.yandex.practicum.commerce.payment.repository.PaymentRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final ShoppingStoreClient shoppingStore;
    private final DeliveryClient delivery;

    @Override
    public PaymentDto doPayment(OrderDto order) {
        PaymentEntity payment = new PaymentEntity();
        payment.setStatus(PaymentStatus.PENDING);
        payment.setFeeTotal(getTotalCoast(order));
        payment.setTotalPayment(getProductsCoast(order));
        payment.setDeliveryTotal(order.deliveryPrice());

        paymentRepository.save(payment);

        return new PaymentDto(payment.getPaymentId(), payment.getTotalPayment(), payment.getDeliveryTotal(),
                payment.getFeeTotal(), payment.getStatus());
    }

    @Override
    public BigDecimal getTotalCoast(OrderDto order) {
        final double TAX_RATE = 1.1;

        if(order.deliveryId() == null || order.deliveryPrice() == null || order.products().isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException("В заказе недостаточно данных для расчёта");
        }

        double coast = getProductsCoastFromStore(order.products()) * TAX_RATE +
            order.deliveryPrice().doubleValue();
        return BigDecimal.valueOf(coast).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void refoundPayment(UUID paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoOrderFoundException("Платёж " + paymentId + " не найден"));
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);
    }

    @Override
    public BigDecimal getProductsCoast(OrderDto order) {
        return BigDecimal.valueOf(getProductsCoastFromStore(order.products()))
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public void failedPayment(UUID paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoOrderFoundException("Платёж " + paymentId + " не найден"));
        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);
    }

    private double getProductsCoastFromStore(Map<UUID, Long> products) {
        final double[] coast = {0.0};
        products.forEach((productId, quantity) -> {
            ProductDto product = shoppingStore.getProduct(productId);
            coast[0] += product.price().doubleValue() * quantity;
        });
        return coast[0];
    }
}
