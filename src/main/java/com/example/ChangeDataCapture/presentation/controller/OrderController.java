package com.example.ChangeDataCapture.presentation.controller;

import com.example.ChangeDataCapture.domain.Order;
import com.example.ChangeDataCapture.infrastructure.inputport.OrderInputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    OrderInputPort orderInputPort;

    @PostMapping(value = "create", produces= MediaType.APPLICATION_JSON_VALUE)
    public Order create(@RequestParam String customerId, @RequestParam BigDecimal total ) {
        return orderInputPort.createOrder(customerId, total);
    }
}
