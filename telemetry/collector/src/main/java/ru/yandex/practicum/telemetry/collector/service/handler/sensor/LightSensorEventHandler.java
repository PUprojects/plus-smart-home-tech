package ru.yandex.practicum.telemetry.collector.service.handler.sensor;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

@Component
public class LightSensorEventHandler extends BaseSensorEventHandler<LightSensorAvro> {
    public LightSensorEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SensorEventProto.PayloadCase getPayload() {
        return SensorEventProto.PayloadCase.LIGHT_SENSOR_EVENT;
    }


    @Override
    protected LightSensorAvro mapToAvro(SensorEventProto event) {
        return LightSensorAvro.newBuilder()
                .setLinkQuality(event.getLightSensorEvent().getLinkQuality())
                .setLuminosity(event.getLightSensorEvent().getLuminosity())
                .build();
    }
}
