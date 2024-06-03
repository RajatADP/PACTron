package com.app.controller;

import com.app.model.Customer;
import com.app.model.Product;
import com.app.model.ProductsResponse;
import com.app.service.RetailerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RetailerController {
    @Autowired
    private RetailerService retailerService;

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "product not found")
    public static class ProductNotFoundException extends RuntimeException { }

    @GetMapping("/products")
    public ProductsResponse allProducts() {
        return retailerService.getAllProductDetails();
    }

    @GetMapping("/product/{id}")
    public Product productById(@PathVariable("id") Long id) {
        return retailerService.getProductDetails(id);
    }

    @GetMapping("/customer/{id}")
    public Customer getCustomerDetails(@PathVariable(value = "id") Long orderId) {
        return retailerService.getCustomerDetails(orderId);
    }
}
