package ru.yandex.practicum.commerce.delivery.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryState;

import java.util.UUID;

@Entity
@Table(name = "delivery")
@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID deliveryId;

    @ManyToOne
    @JoinColumn(name = "from_address_id")
    DeliveryAddress fromAddress;

    @ManyToOne
    @JoinColumn(name = "to_address_id")
    DeliveryAddress toAddress;

    UUID orderId;

    DeliveryState deliveryState;
}
