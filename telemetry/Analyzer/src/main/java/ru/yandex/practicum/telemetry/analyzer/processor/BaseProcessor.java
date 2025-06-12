package ru.yandex.practicum.telemetry.analyzer.processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import ru.yandex.practicum.telemetry.analyzer.repository.ActionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ConditionRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.ScenarioRepository;
import ru.yandex.practicum.telemetry.analyzer.repository.SensorRepository;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Slf4j
public abstract class BaseProcessor implements Runnable{
    protected final Consumer<String, SpecificRecordBase> consumer;
    protected final List<String> topics;
    protected final SensorRepository sensorRepository;
    protected final ActionRepository actionRepository;
    protected final ConditionRepository conditionRepository;
    protected final ScenarioRepository scenarioRepository;


    protected BaseProcessor(Properties properties, List<String> topics,
                            SensorRepository sensorRepository,
                            ActionRepository actionRepository,
                            ConditionRepository conditionRepository,
                            ScenarioRepository scenarioRepository) {
        consumer = new KafkaConsumer<>(properties);
        this.topics = topics;
        this.sensorRepository = sensorRepository;
        this.actionRepository = actionRepository;
        this.conditionRepository = conditionRepository;
        this.scenarioRepository = scenarioRepository;
    }

    @Override
    public void run() {
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
            consumer.subscribe(topics);

            while (true) {
                ConsumerRecords<String, SpecificRecordBase> records = consumer.poll(Duration.ofSeconds(5));
                for (ConsumerRecord<String, SpecificRecordBase> record : records) {
                    processRecord(record.value());
                }
            }

        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от хаба", e);
        } finally {
            try {
                consumer.commitAsync();
            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
            }
        }
    }

    protected abstract void processRecord(SpecificRecordBase record);
}
