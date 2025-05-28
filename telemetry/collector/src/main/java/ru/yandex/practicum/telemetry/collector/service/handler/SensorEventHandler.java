package ru.yandex.practicum.telemetry.collector.service.handler;

import ru.yandex.practicum.telemetry.collector.model.events.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.events.sensor.SensorEventType;

public interface SensorEventHandler {
    SensorEventType getMessageType();

    void handle(SensorEvent event);
}
