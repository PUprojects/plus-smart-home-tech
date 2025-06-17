package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.DeviceRemovedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

@Component
public class DeviceRemovedEventHandler extends BaseHubEventHandler<DeviceRemovedEventAvro> {
    public DeviceRemovedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected DeviceRemovedEventAvro mapToAvro(HubEventProto event) {
        DeviceRemovedEventProto removedEvent = event.getDeviceRemoved();

        return DeviceRemovedEventAvro.newBuilder()
                .setId(removedEvent.getId())
                .build();
    }

    @Override
    public HubEventProto.PayloadCase getPayload() {
        return HubEventProto.PayloadCase.DEVICE_REMOVED;
    }
}