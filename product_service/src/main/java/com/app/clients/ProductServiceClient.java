package com.app.clients;

import com.app.models.Product;
import com.app.models.ProductServiceResponse;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductServiceClient {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${serviceClients.products.baseUrl}")
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public ProductServiceResponse fetchProducts() {
        return restTemplate.getForObject(baseUrl + "/products", ProductServiceResponse.class);
    }

    public Product getProductById(long id) {
        return restTemplate.getForObject(baseUrl + "/product/" + id, Product.class);
    }
}
