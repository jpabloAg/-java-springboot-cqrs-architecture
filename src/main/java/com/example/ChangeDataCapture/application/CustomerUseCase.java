package com.example.ChangeDataCapture.application;

import com.example.ChangeDataCapture.domain.Customer;
import com.example.ChangeDataCapture.infrastructure.inputport.CustomerInputPort;
import com.example.ChangeDataCapture.infrastructure.outputport.CommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class CustomerUseCase implements CustomerInputPort {
    @Autowired
    CommandRepository commandRepository;

    @Override
    public Customer createCustomer(String name, String country) {
        Customer customer = Customer.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .country(country)
                .build();
        return commandRepository.save(customer);
    }

    @Override
    public Customer getById(String customerId) {
        return commandRepository.getById(customerId, Customer.class);
    }

    @Override
    public List<Customer> getAll() {
        return commandRepository.getAll(Customer.class);
    }
}
