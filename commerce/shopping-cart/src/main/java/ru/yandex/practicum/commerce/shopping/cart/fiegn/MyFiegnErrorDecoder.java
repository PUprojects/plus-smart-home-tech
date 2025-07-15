package ru.yandex.practicum.commerce.shopping.cart.fiegn;

import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;

public class MyFiegnErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404) {
            return new NotFoundException("Не найден ресурс для метода " + methodKey);
        }

        if (response.status() == 500) {
            return new InternalServerErrorException("Ошибка сервера при вызове метода " + methodKey);
        }

        return defaultDecoder.decode(methodKey, response);
    }
}
