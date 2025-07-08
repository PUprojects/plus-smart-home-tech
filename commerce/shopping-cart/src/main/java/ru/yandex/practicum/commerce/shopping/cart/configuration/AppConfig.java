package ru.yandex.practicum.commerce.shopping.cart.configuration;

import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.commerce.shopping.cart.fiegn.MyFiegnErrorDecoder;

@Configuration
public class AppConfig {
    @Bean
    public Feign.Builder feignBuilder() {

        return Feign.builder()
                .errorDecoder(new MyFiegnErrorDecoder());  // регистрация кастомного декодера
    }
}
