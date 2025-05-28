package ru.yandex.practicum.telemetry.collector.model.events.hub;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeviceAction {
    String sensorId;
    ActionType type;
    Integer value = null;
}
