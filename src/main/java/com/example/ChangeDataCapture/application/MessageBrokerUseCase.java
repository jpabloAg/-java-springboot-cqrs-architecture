package com.example.ChangeDataCapture.application;

import com.example.ChangeDataCapture.domain.Customer;
import com.example.ChangeDataCapture.domain.Order;
import com.example.ChangeDataCapture.infrastructure.inputport.MessageBrokerInputPort;
import com.example.ChangeDataCapture.infrastructure.outputport.QueryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class MessageBrokerUseCase implements MessageBrokerInputPort {
    @Autowired
    QueryRepository queryRepository;

    Map<String, Class<?>> classes = Map.of(
            "customer", Customer.class,
            "Order", Order.class
    );

    @Override
    public void insertReg(String table, Map<String, Object> reg) {
        queryRepository.save(reg, classes.get(table));
    }

    @Override
    public List<Map<String, Object>> getAll(String table) {
        return queryRepository.getAll(classes.get(table));
    }

    @Override
    public void updateReg(String table, Map<String, Object> reg) {
        queryRepository.save(reg, classes.get(table));
    }

    @Override
    public void deleteReg(String table, Map<String, Object> reg) {
        queryRepository.delete((String)reg.get("id"), classes.get( table ) );
    }
}
