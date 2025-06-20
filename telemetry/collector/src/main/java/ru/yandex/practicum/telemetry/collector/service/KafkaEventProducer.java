package ru.yandex.practicum.telemetry.collector.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.serializer.GeneralAvroSerializer;

import java.util.Properties;

@Slf4j
@Component
public class KafkaEventProducer {
    private final Producer<String, SpecificRecordBase> producer;

    public KafkaEventProducer() {
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneralAvroSerializer.class);
        config.put(ProducerConfig.CLIENT_ID_CONFIG, "telemetry.collector");
        this.producer = new KafkaProducer<>(config);
    }

    public void sendEvent(String topic, SpecificRecordBase message) {
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, message);
        producer.send(record);
        producer.flush();
        log.info("Отправлено собщение в топик {} -- {}", topic, message.getSchema().getFullName());
    }
}