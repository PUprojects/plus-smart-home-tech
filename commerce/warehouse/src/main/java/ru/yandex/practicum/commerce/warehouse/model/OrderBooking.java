package ru.yandex.practicum.commerce.warehouse.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderBooking {
    @Id
    UUID orderId;

    UUID deliveryId;

    @ElementCollection
    @CollectionTable(name="order_products", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    Map<UUID, Long> products = new HashMap<>();
}
