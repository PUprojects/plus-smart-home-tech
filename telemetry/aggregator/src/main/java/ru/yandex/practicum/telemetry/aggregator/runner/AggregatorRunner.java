package ru.yandex.practicum.telemetry.aggregator.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.telemetry.aggregator.service.AggregationStarter;

@Component
@RequiredArgsConstructor
public class AggregatorRunner implements CommandLineRunner {
    private final AggregationStarter aggregationStarter;

    @Override
    public void run(String... args) throws Exception {
        aggregationStarter.start();
    }
}
