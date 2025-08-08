package ru.yandex.practicum.commerce.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.commerce.delivery.feign.OrderClient;
import ru.yandex.practicum.commerce.delivery.feign.WarehouseClient;
import ru.yandex.practicum.commerce.delivery.model.DeliveryAddress;
import ru.yandex.practicum.commerce.delivery.model.DeliveryEntity;
import ru.yandex.practicum.commerce.delivery.repository.AddressRepository;
import ru.yandex.practicum.commerce.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.commerce.delivery.service.mapper.DeliveryMapper;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryDto;
import ru.yandex.practicum.commerce.dto.delivery.DeliveryState;
import ru.yandex.practicum.commerce.dto.order.OrderDto;
import ru.yandex.practicum.commerce.dto.warehouse.AddressDto;
import ru.yandex.practicum.commerce.exception.delivery.NoDeliveryFoundException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final AddressRepository addressRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderClient orderClient;
    private final WarehouseClient warehouseClient;

    @Override
    public DeliveryDto addDelivery(DeliveryDto newDelivery) {
        DeliveryEntity delivery = new DeliveryEntity();

        delivery.setDeliveryState(newDelivery.deliveryState());
        delivery.setOrderId(newDelivery.orderId());

        DeliveryAddress fromAddress = addressRepository.findByCountryAndCityAndStreetAndHouseAndFlat(
                newDelivery.fromAddress().country(), newDelivery.fromAddress().city(),
                newDelivery.fromAddress().street(), newDelivery.fromAddress().house(),
                newDelivery.fromAddress().flat())
                        .orElseGet(() -> new DeliveryAddress(null, newDelivery.fromAddress().country(), newDelivery.fromAddress().city(),
                newDelivery.fromAddress().street(), newDelivery.fromAddress().house(),
                newDelivery.fromAddress().flat()));
        delivery.setFromAddress(fromAddress);

        DeliveryAddress toAddress = addressRepository.findByCountryAndCityAndStreetAndHouseAndFlat(
                newDelivery.toAddress().country(), newDelivery.toAddress().city(),
                newDelivery.toAddress().street(), newDelivery.toAddress().house(),
                newDelivery.toAddress().flat())
                        .orElseGet(() -> new DeliveryAddress(null, newDelivery.toAddress().country(), newDelivery.toAddress().city(),
                newDelivery.toAddress().street(), newDelivery.toAddress().house(),
                newDelivery.toAddress().flat()));
        delivery.setToAddress(toAddress);

        delivery = deliveryRepository.save(delivery);

        return deliveryMapper.toDto(delivery);
    }

    @Override
    public void successfulDelivery(UUID deliveryId) {
        DeliveryEntity delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NoDeliveryFoundException("Не найдена доставка с идентификатором " + deliveryId));

        delivery.setDeliveryState(DeliveryState.DELIVERED);
        deliveryRepository.save(delivery);

        orderClient.completed(delivery.getOrderId());
    }

    @Override
    public void pickedDelivery(UUID deliveryId) {
        DeliveryEntity delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NoDeliveryFoundException("Не найдена доставка с идентификатором " + deliveryId));

        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        deliveryRepository.save(delivery);

        orderClient.assembly(delivery.getOrderId());
    }

    @Override
    public void filedDelivery(UUID deliveryId) {
        DeliveryEntity delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NoDeliveryFoundException("Не найдена доставка с идентификатором " + deliveryId));

        delivery.setDeliveryState(DeliveryState.FAILED);
        deliveryRepository.save(delivery);
    }

    @Override
    public BigDecimal getDeliveryCoast(OrderDto order) {
        DeliveryEntity delivery = deliveryRepository.findById(order.deliveryId())
                .orElseThrow(() -> new NoDeliveryFoundException("Не найдена доставка с идентификатором " + order.deliveryId()));

        double coast = 5.0;
        AddressDto warehouseAddress = warehouseClient.getWarehouseAddress();
        if(warehouseAddress.city().contains("ADDRESS_1")) {
            coast *= 2;
        }
        if(warehouseAddress.city().contains("ADDRESS_2")){
            coast *= 3;
        }
        if(order.fragile()) {
            coast *= 1.2;
        }

        coast += order.deliveryWeight() * 0.3;
        coast += order.deliveryVolume() * 0.2;

        if(!warehouseAddress.street().equals(delivery.getToAddress().getStreet())) {
            coast *= 1.2;
        }

        return BigDecimal.valueOf(coast).setScale(2, RoundingMode.UP);
    }
}
