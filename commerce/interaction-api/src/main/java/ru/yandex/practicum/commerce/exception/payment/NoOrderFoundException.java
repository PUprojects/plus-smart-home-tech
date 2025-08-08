package ru.yandex.practicum.commerce.exception.payment;

public class NoOrderFoundException extends RuntimeException {
    public NoOrderFoundException(String message) { super(message); }
}
