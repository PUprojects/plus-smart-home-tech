package ru.yandex.practicum.commerce.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.commerce.delivery.model.DeliveryAddress;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<DeliveryAddress, Long> {
    Optional<DeliveryAddress> findByCountryAndCityAndStreetAndHouseAndFlat(String country, String city,
                                                                           String street, String house, String flat);
}
