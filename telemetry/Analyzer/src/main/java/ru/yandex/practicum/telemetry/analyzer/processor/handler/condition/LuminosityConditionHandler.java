package ru.yandex.practicum.telemetry.analyzer.processor.handler.condition;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Component
public class LuminosityConditionHandler extends BaseConditionHandler {
    @Override
    public boolean isConditionValid(Condition condition, SensorStateAvro sensorState) {
        if(!(sensorState.getData() instanceof LightSensorAvro lightSensor)) {
            return false;
        }

        return compareIntegerConditionValues(condition, lightSensor.getLuminosity());
    }

    @Override
    public ConditionTypeAvro getType() {
        return ConditionTypeAvro.LUMINOSITY;
    }
}
