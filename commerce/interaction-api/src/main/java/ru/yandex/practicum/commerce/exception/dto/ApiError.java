package ru.yandex.practicum.commerce.exception.dto;

import org.springframework.http.HttpStatus;

public record ApiError(HttpStatus status) {
}
