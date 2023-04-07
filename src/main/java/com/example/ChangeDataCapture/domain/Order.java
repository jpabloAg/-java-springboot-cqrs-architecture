package com.example.ChangeDataCapture.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class Order {
    private String id;
    private String customerId;
    private BigDecimal total;
}
