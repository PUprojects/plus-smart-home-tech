package ru.yandex.practicum.telemetry.analyzer.processor.handler.condition;

import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

public interface ConditionHandler {
    boolean isConditionValid(Condition condition, SensorStateAvro sensorState);

    ConditionTypeAvro getType();
}
