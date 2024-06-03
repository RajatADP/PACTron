package com.app.controller;

import com.app.clients.CustomerServiceClient;
import com.app.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerServiceController {
    @Autowired
    private CustomerServiceClient customerServiceClient;

    @GetMapping("/customerDetails/{id}")
    public Customer customerDetails(@PathVariable("id") Long id) {
        return customerServiceClient.getCustomerById(id);
    }
}
