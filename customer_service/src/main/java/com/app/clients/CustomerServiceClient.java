package com.app.clients;

import com.app.models.Customer;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomerServiceClient {
    @Autowired
    private RestTemplate restTemplate;

    @Setter
    @Value("${serviceClients.products.baseUrl}")
    private String baseUrl;

    public Customer getCustomerById(long id) {
        return restTemplate.getForObject(baseUrl + "/customer/" + id, Customer.class);
    }
}
