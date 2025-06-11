package ru.yandex.practicum.telemetry.analyzer.configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

@Getter
@Setter
@ToString
@Component
@ConfigurationProperties("analyzer.kafka")
public class KafkaConfig implements HubEventConsumerConfig, SnapshotConsumerConfig {
    private ConsumerConfig snapshotConsumer;
    private ConsumerConfig hubEventConsumer;

    @Override
    public Properties getHubEventConsumerProperties() {
        return hubEventConsumer.properties();
    }

    @Override
    public List<String> getHubEventConsumerTopics() {
        return hubEventConsumer.topics();
    }

    @Override
    public Properties getSnapshotConsumerProperties() {
        return snapshotConsumer.properties();
    }

    @Override
    public List<String> getSnapshotConsumerTopics() {
        return snapshotConsumer.topics();
    }

    public record ConsumerConfig(Properties properties, List<String> topics) {
    }
}
