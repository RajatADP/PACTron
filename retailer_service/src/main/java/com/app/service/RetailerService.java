package com.app.service;

import com.app.controller.RetailerController;
import com.app.model.Customer;
import com.app.model.Product;
import com.app.model.ProductsResponse;
import com.app.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetailerService {
    @Autowired
    private ProductRepository productRepository;
    public Product getProductDetails(Long productId) {
        return productRepository.findById(productId).orElseThrow(RetailerController.ProductNotFoundException::new);
    }

    public ProductsResponse getAllProductDetails() {
        return new ProductsResponse((List<Product>) productRepository.findAll());
    }

    public Customer getCustomerDetails(Long orderId) {
        return new Customer(1L, "John");
    }
}
