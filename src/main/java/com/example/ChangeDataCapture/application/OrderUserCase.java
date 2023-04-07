package com.example.ChangeDataCapture.application;

import com.example.ChangeDataCapture.domain.Order;
import com.example.ChangeDataCapture.infrastructure.inputport.OrderInputPort;
import com.example.ChangeDataCapture.infrastructure.outputport.CommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class OrderUserCase implements OrderInputPort {
    @Autowired
    CommandRepository commandRepository;

    @Override
    public Order createOrder(String customerId, BigDecimal total) {
        Order order = Order.builder()
                .id(UUID.randomUUID().toString())
                .customerId(customerId)
                .total(total)
                .build();
        return commandRepository.save(order);
    }
}
