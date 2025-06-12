package ru.yandex.practicum.telemetry.analyzer.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "sensors")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Sensor {
    @Id
    String id;

    String hubId;
}
