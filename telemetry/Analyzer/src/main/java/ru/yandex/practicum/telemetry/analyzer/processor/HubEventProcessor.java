package ru.yandex.practicum.telemetry.analyzer.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.telemetry.analyzer.configuration.HubEventConsumerConfig;
import ru.yandex.practicum.telemetry.analyzer.model.Action;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.model.Sensor;
import ru.yandex.practicum.telemetry.analyzer.repository.ActionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ConditionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

@Slf4j
@Component
public class HubEventProcessor extends BaseProcessor {

    @Autowired
    public HubEventProcessor (HubEventConsumerConfig config,
                              SensorRepository sensorRepository,
                              ActionRepository actionRepository,
                              ConditionRepository conditionRepository,
                              ScenarioRepository scenarioRepository) {
        super(config.getHubEventConsumerProperties(), config.getHubEventConsumerTopics(),
                sensorRepository, actionRepository, conditionRepository, scenarioRepository);

    }

    @Override
    protected void processRecord(SpecificRecordBase record) {
        if(!(record instanceof HubEventAvro hubEvent)) {
            return;
        }

        switch (hubEvent.getPayload()) {
            case DeviceAddedEventAvro deviceAddedEvent -> addDevice(deviceAddedEvent, hubEvent.getHubId());
            case DeviceRemovedEventAvro deviceRemovedEvent -> removeDevice(deviceRemovedEvent);
            case ScenarioAddedEventAvro scenarioAddedEvent -> addScenario(scenarioAddedEvent, hubEvent.getHubId());
            case ScenarioRemovedEventAvro scenarioRemovedEvent -> removeScenario(scenarioRemovedEvent, hubEvent.getHubId());
            default -> throw new IllegalStateException("Unexpected value: " + hubEvent.getPayload());
        }
    }

    private void addDevice(DeviceAddedEventAvro deviceAddedEvent, String hubId) {

        Sensor sensor = new Sensor();
        sensor.setId(deviceAddedEvent.getId());
        sensor.setHubId(hubId);

        sensorRepository.save(sensor);
        log.info("Добавлено устройство {} в хаб {}", deviceAddedEvent.getId(), hubId);
    }

    private void removeDevice(DeviceRemovedEventAvro deviceRemovedEvent) {
        sensorRepository.findById(deviceRemovedEvent.getId()).ifPresent(sensorRepository::delete);
        log.info("Удалено устройство {}", deviceRemovedEvent.getId());
    }

    @Transactional
    private void addScenario(ScenarioAddedEventAvro scenarioAddedEvent, String hubId) {
        Scenario scenario = new Scenario();
        scenario.setName(scenarioAddedEvent.getName());
        scenario.setHubId(hubId);

        scenarioAddedEvent.getConditions().forEach(scenarioCondition -> {
            Condition condition = new Condition();
            condition.setType(scenarioCondition.getType().toString());
            condition.setOperation(scenarioCondition.getOperation().toString());
            switch (scenarioCondition.getValue()) {
                case null -> condition.setVal(0);
                case Integer i -> condition.setVal(i);
                case Boolean b -> condition.setVal(b ? 1 : 0);
                default -> throw new IllegalStateException("Unexpected value: " + scenarioCondition.getValue());
            }
            condition = conditionRepository.save(condition);
            scenario.getConditions().put(scenarioCondition.getSensorId(), condition);
            log.info("Добавлено условие {}", condition);
        });

        scenarioAddedEvent.getActions().forEach(deviceAction -> {
            Action action = new Action();
            action.setType(deviceAction.getType().toString());
            action.setVal(deviceAction.getValue());
            action = actionRepository.save(action);
            scenario.getActions().put(deviceAction.getSensorId(), action);
            log.info("Добавлено действие {}", action);
        });

        scenarioRepository.save(scenario);
        log.info("Добавлен сценарий {} для хаба {}", scenarioAddedEvent.getName(), hubId);
    }

    private void removeScenario(ScenarioRemovedEventAvro scenarioRemovedEvent, String hubId) {
        scenarioRepository.findByHubIdAndName(scenarioRemovedEvent.getName(), hubId)
                .ifPresent(scenarioRepository::delete);
        log.info("Удален сценарий {}", scenarioRemovedEvent.getName());
    }
}
