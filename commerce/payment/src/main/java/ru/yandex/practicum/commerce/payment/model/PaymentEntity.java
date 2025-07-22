package ru.yandex.practicum.commerce.payment.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.dto.payment.PaymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID paymentId;

    BigDecimal totalPayment;

    BigDecimal deliveryTotal;

    BigDecimal feeTotal;

    PaymentStatus status;
}
