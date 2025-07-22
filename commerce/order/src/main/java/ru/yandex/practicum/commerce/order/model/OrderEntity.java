package ru.yandex.practicum.commerce.order.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.dto.shopping.cart.OrderState;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID orderId;

    UUID shoppingCartId;

    UUID paymentId;

    UUID deliveryId;

    OrderState state;

    String username;

    Double deliveryWeight;

    Double deliveryVolume;

    Boolean fragile;

    BigDecimal totalPrice;

    BigDecimal deliveryPrice;

    BigDecimal productPrice;

    @ElementCollection
    @CollectionTable(name="order_products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    Map<UUID, Long> products = new HashMap<>();
}
