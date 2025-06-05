package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.service.handler.SensorEventHandler;

@Component
public class SwitchSensorEventHandler implements SensorEventHandler {
    @Override
    public SensorEventProto.PayloadCase getPayload() {
        return SensorEventProto.PayloadCase.SWITCH_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {

    }
}
