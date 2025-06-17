package ru.yandex.practicum.telemetry.analyzer.processor;

import com.google.protobuf.Timestamp;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import ru.yandex.practicum.telemetry.analyzer.configuration.SnapshotConsumerConfig;
import ru.yandex.practicum.telemetry.analyzer.model.Scenario;
import ru.yandex.practicum.telemetry.analyzer.processor.handler.condition.ConditionHandler;
import ru.yandex.practicum.telemetry.analyzer.repository.ActionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ConditionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SnapshotProcessor extends BaseProcessor {
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;
    private final Map<ConditionTypeAvro, ConditionHandler> conditionHandlers;

    @Autowired
    public SnapshotProcessor(SnapshotConsumerConfig config,
                             @GrpcClient("hub-router")
                             HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient,
                             SensorRepository sensorRepository,
                             ActionRepository actionRepository,
                             ConditionRepository conditionRepository,
                             ScenarioRepository scenarioRepository,
                             Set<ConditionHandler> conditionHandlers) {
        super(config.getSnapshotConsumerProperties(), config.getSnapshotConsumerTopics(),
                sensorRepository, actionRepository, conditionRepository, scenarioRepository);
        this.hubRouterClient = hubRouterClient;
        this.conditionHandlers = conditionHandlers.stream()
                .collect(Collectors.toMap(ConditionHandler::getType, Function.identity()));
    }

    @Override
    protected void processRecord(SpecificRecordBase record) {
        if (!(record instanceof SensorsSnapshotAvro sensorsSnapshot)) {
            return;
        }

        log.info("Получен снапшот для хаба {}", sensorsSnapshot.getHubId());

        List<Scenario> scenarios = scenarioRepository.findByHubId(sensorsSnapshot.getHubId());

        log.info("Количество сценариев {}", scenarios.size());

        for(Scenario scenario : scenarios) {
            log.info("Проеверяем условия сценария {}", scenario.getName());
            long validConditionsCount = scenario.getConditions().entrySet().stream()
                    .filter(entry -> {
                        log.info("Сенсор {} условие {}", entry.getKey(), entry.getValue());
                        SensorStateAvro sensorState = sensorsSnapshot.getSensorsState().get(entry.getKey());
                        if (sensorState == null) {
                            log.info("Нет состояния сенсора");
                            return false;
                        }

                        ConditionTypeAvro handlerType = ConditionTypeAvro.valueOf(entry.getValue().getType());
                        ConditionHandler handler = conditionHandlers.get(handlerType);
                        if (handler == null) {
                            log.info("Нет обработчика");
                            return false;
                        }
                        boolean result = handler.isConditionValid(entry.getValue(), sensorState);
                        log.info("Результат {}", result);
                        return result;
                    })
                    .count();
            log.info("Выполнено условий {} из {}", validConditionsCount, scenario.getConditions().size());
            if(scenario.getConditions().size() != validConditionsCount) {
                continue;
            }

            log.info("Выполняем сценарий {}", scenario.getName());

            scenario.getActions().forEach((sensorId, action) -> {
                DeviceActionProto.Builder actionBuilder = DeviceActionProto.newBuilder();
                actionBuilder.setSensorId(sensorId)
                        .setType(ActionTypeProto.valueOf(action.getType()));
                if(action.getVal() != null) {
                    actionBuilder.setValue(action.getVal());
                }
                DeviceActionProto deviceActionProto = actionBuilder.build();

                Instant actionInstant = Instant.now();
                Timestamp actionTimestamp = Timestamp.newBuilder()
                        .setSeconds(actionInstant.getEpochSecond())
                        .setNanos(actionInstant.getNano())
                        .build();

                DeviceActionRequest deviceActionRequest = DeviceActionRequest.newBuilder()
                        .setHubId(sensorsSnapshot.getHubId())
                        .setScenarioName(scenario.getName())
                        .setAction(deviceActionProto)
                        .setTimestamp(actionTimestamp)
                        .build();

                hubRouterClient.handleDeviceAction(deviceActionRequest);

                log.info("Отправили данные");

            });
        }
    }
}
