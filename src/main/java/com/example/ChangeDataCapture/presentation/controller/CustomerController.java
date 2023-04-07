package com.example.ChangeDataCapture.presentation.controller;

import com.example.ChangeDataCapture.domain.Customer;
import com.example.ChangeDataCapture.infrastructure.inputport.CustomerInputPort;
import com.example.ChangeDataCapture.infrastructure.inputport.MessageBrokerInputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    CustomerInputPort customerInputPort;

    @Autowired
    MessageBrokerInputPort messageBrokerInputPort;

    @PostMapping(value = "create", produces= MediaType.APPLICATION_JSON_VALUE)
    public Customer create(@RequestParam String name, @RequestParam String country ) {
        return customerInputPort.createCustomer(name, country);
    }

    @PostMapping(value = "get", produces=MediaType.APPLICATION_JSON_VALUE)
    public Customer get( @RequestParam String customerId ) {
        return customerInputPort.getById(customerId);
    }

    @PostMapping(value = "getall", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Customer> getAll() {
        return customerInputPort.getAll();
    }

    @PostMapping(value = "getallCustomerCQRS", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String,Object>> getallCQRS() {
        return messageBrokerInputPort.getAll( "customer" );
    }
}
