package ru.yandex.practicum.telemetry.analyzer.processor.handler.condition;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;
import ru.yandex.practicum.telemetry.analyzer.model.Condition;

@Slf4j
public abstract class BaseConditionHandler implements ConditionHandler{
    protected boolean compareIntegerConditionValues(Condition condition, Integer value) {
        ConditionOperationAvro operation = ConditionOperationAvro.valueOf(condition.getOperation());

        log.info("Сравнение integer {} {} {}", value, operation, condition.getVal());

        switch (operation) {
            case EQUALS -> {
                return value.equals(condition.getVal());
            }
            case LOWER_THAN -> {
                return value.compareTo(condition.getVal()) < 0;
            }
            case GREATER_THAN -> {
                return value.compareTo(condition.getVal()) > 0;
            }
        }

        return false;
    }

    protected boolean compareBooleanCondition(Condition condition, Boolean value) {

        log.info("Сравнение boolean {} {} {}", value, condition.getOperation(), condition.getVal());

        if(condition.getOperation().equals(ConditionOperationAvro.EQUALS.toString())) {
            boolean conditionValue = condition.getVal() != 0;
            return value.equals(conditionValue);
        }

        return false;
    }
}
