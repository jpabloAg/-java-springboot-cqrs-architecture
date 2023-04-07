package com.example.ChangeDataCapture.infrastructure.inputport;

import com.example.ChangeDataCapture.domain.Order;

import java.math.BigDecimal;

public interface OrderInputPort {
    public Order createOrder(String customerId, BigDecimal total);
}
