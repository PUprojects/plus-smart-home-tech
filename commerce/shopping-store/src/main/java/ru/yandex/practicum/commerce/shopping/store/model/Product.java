package ru.yandex.practicum.commerce.shopping.store.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductCategory;
import ru.yandex.practicum.commerce.dto.shopping.store.ProductState;
import ru.yandex.practicum.commerce.dto.shopping.store.QuantityState;

import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID productId;

    String productName;

    String description;

    String imageSrc;

    @Enumerated(EnumType.STRING)
    QuantityState quantityState;

    @Enumerated(EnumType.STRING)
    ProductState productState;

    @Enumerated(EnumType.STRING)
    ProductCategory productCategory;

    float price;
}
