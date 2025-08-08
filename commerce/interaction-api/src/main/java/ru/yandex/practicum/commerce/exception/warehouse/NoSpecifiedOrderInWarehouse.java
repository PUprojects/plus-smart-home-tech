package ru.yandex.practicum.commerce.exception.warehouse;

public class NoSpecifiedOrderInWarehouse extends RuntimeException {
    public NoSpecifiedOrderInWarehouse(String message) { super(message); }
}
