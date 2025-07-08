package ru.yandex.practicum.commerce.warehouse.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Table(name = "warehouse")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseProduct {
    @Id
    UUID productId;

    Boolean fragile;

    Double width;

    Double height;

    Double depth;

    Double weight;

    Long quantity;
}
