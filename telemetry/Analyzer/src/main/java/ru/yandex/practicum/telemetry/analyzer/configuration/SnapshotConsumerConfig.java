package ru.yandex.practicum.telemetry.analyzer.configuration;

import java.util.List;
import java.util.Properties;

public interface SnapshotConsumerConfig {
    Properties getSnapshotConsumerProperties();

    List<String> getSnapshotConsumerTopics();
}
