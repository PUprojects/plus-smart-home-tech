package ru.yandex.practicum.telemetry.collector.service.handler.hub;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.collector.service.KafkaEventProducer;

import java.util.List;

@Component
public class ScenarioAddedEventHandler extends BaseHubEventHandler<ScenarioAddedEventAvro> {
    public ScenarioAddedEventHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    protected ScenarioAddedEventAvro mapToAvro(HubEventProto event) {
        ScenarioAddedEventProto addedEvent = event.getScenarioAdded();

        List<DeviceActionAvro> actions = addedEvent.getActionList().stream()
                .map(action -> DeviceActionAvro.newBuilder()
                        .setSensorId(action.getSensorId())
                        .setType(ActionTypeAvro.valueOf(action.getType().toString()))
                        .build())
                .toList();

        List<ScenarioConditionAvro> conditions = addedEvent.getConditionList().stream()
                .map(condition -> ScenarioConditionAvro.newBuilder()
                        .setSensorId(condition.getSensorId())
                        .setValue(condition.getValueCase().equals(ScenarioConditionProto.ValueCase.BOOL_VALUE) ?
                                condition.getBoolValue() : condition.getIntValue())
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
    public HubEventProto.PayloadCase getPayload() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }
}