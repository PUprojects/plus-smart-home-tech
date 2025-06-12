package ru.yandex.practicum.telemetry.analyzer.processor.handler.condition;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Component
public class TemperatureConditionHandler extends BaseConditionHandler {

    @Override
    public boolean isConditionValid(Condition condition, SensorStateAvro sensorState) {

        if(!(sensorState.getData() instanceof ClimateSensorAvro climateSensor)) {
            return false;
        }

        return compareIntegerConditionValues(condition, climateSensor.getTemperatureC());
    }

    @Override
    public ConditionTypeAvro getType() {
        return ConditionTypeAvro.TEMPERATURE;
    }
}
