package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.collector.model.events.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.events.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.events.hub.ScenarioAddedEvent;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

import java.util.List;

@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {
    public ScenarioAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEvent event) {
        ScenarioAddedEvent addedEvent = (ScenarioAddedEvent) event;


        List<DeviceActionAvro> actions = addedEvent.getActions().stream()
                .map(action -> DeviceActionAvro.newBuilder()
                        .setSensorId(action.getSensorId())
                        .setType(ActionTypeAvro.valueOf(action.getType().toString()))
                        .build())
                .toList();

        List<ScenarioConditionAvro> conditions = addedEvent.getConditions().stream()
                .map(condition -> ScenarioConditionAvro.newBuilder()
                        .setSensorId(condition.getSensorId())
                        .setValue(condition.getValue())
                        .setType(ConditionTypeAvro.valueOf(condition.getType().toString()))
                        .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().toString()))
                        .build())
                .toList();

        return ScenarioAddedEventAvro.newBuilder()
                .setName(addedEvent.getName())
                .setActions(actions)
                .setConditions(conditions)
                .build();
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
