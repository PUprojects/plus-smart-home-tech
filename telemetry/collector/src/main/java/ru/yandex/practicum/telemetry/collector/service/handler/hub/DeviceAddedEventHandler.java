package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

@Component
public class DeviceAddedEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> {
    public DeviceAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected DeviceAddedEventAvro mapToAvro(HubEventProto event) {
        DeviceAddedEventProto addedEvent = event.getDeviceAdded();
        return DeviceAddedEventAvro.newBuilder()
                .setType(DeviceTypeAvro.valueOf(addedEvent.getType().toString()))
                .setId(addedEvent.getId())
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getPayload() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }
}