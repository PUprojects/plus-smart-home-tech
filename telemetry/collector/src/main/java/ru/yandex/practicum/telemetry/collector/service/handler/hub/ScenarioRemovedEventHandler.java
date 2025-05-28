package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;
import ru.yandex.practicum.telemetry.collector.model.events.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.events.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.events.hub.ScenarioRemovedEvent;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

public class ScenarioRemovedEventHandler extends BaseHubEventHandler<ScenarioRemovedEventAvro> {
    public ScenarioRemovedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ScenarioRemovedEventAvro mapToAvro(HubEvent event) {
        ScenarioRemovedEvent removedEvent = (ScenarioRemovedEvent) event;

        return ScenarioRemovedEventAvro.newBuilder()
                .setName(removedEvent.getName())
                .build();
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
