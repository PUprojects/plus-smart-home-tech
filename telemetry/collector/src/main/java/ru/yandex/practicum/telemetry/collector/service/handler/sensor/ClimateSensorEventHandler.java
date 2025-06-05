package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.service.handler.SensorEventHandler;

@Component
public class ClimateSensorEventHandler implements SensorEventHandler {
    @Override
    public SensorEventProto.PayloadCase getPayload() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }

    @Override
    public void handle(SensorEventProto event) {

    }
}
