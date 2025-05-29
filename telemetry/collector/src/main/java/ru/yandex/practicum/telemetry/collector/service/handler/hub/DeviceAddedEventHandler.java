package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.telemetry.collector.model.events.hub.DeviceAddedEvent;
import ru.yandex.practicum.telemetry.collector.model.events.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.events.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

@Component
public class DeviceAddedEventHandler extends BaseHubEventHandler<DeviceAddedEventAvro> {
    public DeviceAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected DeviceAddedEventAvro mapToAvro(HubEvent event) {
        DeviceAddedEvent addedEvent = (DeviceAddedEvent) event;
        return DeviceAddedEventAvro.newBuilder()
                .setType(DeviceTypeAvro.valueOf(addedEvent.getDeviceType().toString()))
                .setId(addedEvent.getId())
                .build();
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_ADDED;
    }
}
