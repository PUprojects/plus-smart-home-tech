package ru.yandex.practicum.telemetry.analyzer.processor.handler.condition;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Component
public class SwitchConditionHandler extends BaseConditionHandler {
    @Override
    public boolean isConditionValid(Condition condition, SensorStateAvro sensorState) {
        if(!(sensorState.getData() instanceof SwitchSensorAvro switchSensor)) {
            return false;
        }

        return compareBooleanCondition(condition, switchSensor.getState());
    }

    @Override
    public ConditionTypeAvro getType() {
        return ConditionTypeAvro.SWITCH;
    }
}
