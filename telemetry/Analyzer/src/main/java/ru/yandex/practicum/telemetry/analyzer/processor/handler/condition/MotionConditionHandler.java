package ru.yandex.practicum.telemetry.analyzer.processor.handler.condition;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Component
public class MotionConditionHandler extends BaseConditionHandler {
    @Override
    public boolean isConditionValid(Condition condition, SensorStateAvro sensorState) {
        if(!(sensorState.getData() instanceof MotionSensorAvro motionSensor)) {
            return  false;
        }

        return compareBooleanCondition(condition, motionSensor.getMotion());
    }

    @Override
    public ConditionTypeAvro getType() {
        return ConditionTypeAvro.MOTION;
    }
}
