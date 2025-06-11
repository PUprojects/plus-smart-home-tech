package ru.yandex.practicum.telemetry.analyzer.processor;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.configuration.SnapshotConsumerConfig;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.repository.ActionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ConditionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

import java.util.List;

@Slf4j
@Component
public class SnapshotProcessor extends BaseProcessor {
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    @Autowired
    public SnapshotProcessor(SnapshotConsumerConfig config,
                             @GrpcClient("hub-router")
                             HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient,
                             SensorRepository sensorRepository,
                             ActionRepository actionRepository,
                             ConditionRepository conditionRepository,
                             ScenarioRepository scenarioRepository) {
        super(config.getSnapshotConsumerProperties(), config.getSnapshotConsumerTopics(),
                sensorRepository, actionRepository, conditionRepository, scenarioRepository);
        this.hubRouterClient = hubRouterClient;
    }

    @Override
    protected void processRecord(SpecificRecordBase record) {
        if (!(record instanceof SensorsSnapshotAvro sensorsSnapshot)) {
            return;
        }

        List<Scenario> scenarios = scenarioRepository.findByHubId(sensorsSnapshot.getHubId());
        for(Scenario scenario : scenarios) {
            scenario.getConditions().forEach((sensorId, condition) -> {
                SensorStateAvro sensorState = sensorsSnapshot.getSensorsState().get(sensorId);
                if (sensorState == null) {
                    return;
                }

            });
        }
    }
}
