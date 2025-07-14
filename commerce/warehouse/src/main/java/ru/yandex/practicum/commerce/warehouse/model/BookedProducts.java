package ru.yandex.practicum.commerce.warehouse.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookedProducts {
    Double deliveryWeight = 0.0;

    Double deliveryVolume = 0.0;

    Boolean fragile = false;
}
