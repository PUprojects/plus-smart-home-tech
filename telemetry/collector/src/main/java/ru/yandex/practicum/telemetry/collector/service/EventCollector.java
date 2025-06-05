package ru.yandex.practicum.telemetry.collector.service;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.telemetry.collector.service.handler.SensorEventHandler;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@GrpcService
public class EventCollector extends CollectorControllerGrpc.CollectorControllerImplBase {
    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> sensorEventHandlers;

    public EventCollector(Set<SensorEventHandler> sensorEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getPayload, Function.identity()));
    }

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            SensorEventHandler handler = sensorEventHandlers.get(request.getPayloadCase());
            if(handler == null) {
                throw new IllegalArgumentException("Не могу найти обработчик для события " + request.getPayloadCase());
            }

            handler.handle(request);

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }
}
