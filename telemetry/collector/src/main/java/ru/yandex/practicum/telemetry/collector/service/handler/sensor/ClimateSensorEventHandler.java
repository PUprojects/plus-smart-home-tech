package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

@Slf4j
@Component
public class ClimateSensorEventHandler extends BaseSensorEventHandler<ClimateSensorAvro> {
    public ClimateSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ClimateSensorAvro mapToAvro(SensorEventProto event) {
        ClimateSensorAvro result = ClimateSensorAvro.newBuilder()
                .setCo2Level(event.getClimateSensorEvent().getCo2Level())
                .setHumidity(event.getClimateSensorEvent().getHumidity())
                .setTemperatureC(event.getClimateSensorEvent().getTemperatureC())
                .build();
        return result;
    }
    @Override
    public SensorEventProto.PayloadCase getPayload() {
        return SensorEventProto.PayloadCase.CLIMATE_SENSOR_EVENT;
    }
}


