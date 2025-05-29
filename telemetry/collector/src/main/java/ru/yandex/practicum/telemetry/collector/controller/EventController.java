package ru.yandex.practicum.telemetry.collector.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.telemetry.collector.model.events.hub.HubEvent;
import ru.yandex.practicum.telemetry.collector.model.events.hub.HubEventType;
import ru.yandex.practicum.telemetry.collector.model.events.sensor.SensorEvent;
import ru.yandex.practicum.telemetry.collector.model.events.sensor.SensorEventType;
import ru.yandex.practicum.telemetry.collector.service.handler.HubEventHandler;
import ru.yandex.practicum.telemetry.collector.service.handler.SensorEventHandler;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Validated
@RequestMapping(path = "/events", consumes = MediaType.APPLICATION_JSON_VALUE)
public class EventController {
    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlers;
    private final Map<HubEventType, HubEventHandler> hubEventHandlers;

    public EventController(List<SensorEventHandler> sensorEventHandlers,
                           List<HubEventHandler> hubEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
    }

    @PostMapping("/sensors")
    public void collectSensorEvent(@RequestBody @Valid SensorEvent request) {
        log.info("Получено событие сенсора {}", request.getType());
        SensorEventHandler handler = sensorEventHandlers.get(request.getType());
        if (handler == null) {
            throw new IllegalArgumentException("Не могу найти обработчик для события " + request.getType());
        }

        handler.handle(request);
    }

    @PostMapping("/hubs")
    public void collectHubEvent(@RequestBody @Valid HubEvent request) {
        log.info("Получено событие хаба {}", request.getType());
        HubEventHandler handler = hubEventHandlers.get(request.getType());
        if (handler == null) {
            throw new IllegalArgumentException("Не могу найти обработчик для события " + request.getType());
        }

        handler.handle(request);
    }
}
