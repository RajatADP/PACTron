package com.app.controller;

import com.app.clients.ProductServiceClient;
import com.app.models.Product;
import com.app.models.ProductCatalogue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductCatalogueController {
    @Autowired
    private ProductServiceClient productServiceClient;

    @GetMapping("/catalogue")
    public ProductCatalogue catalogue() {
        return new ProductCatalogue(productServiceClient.fetchProducts().getProducts());
    }

    @GetMapping("/catalogue/{id}")
    public Product catalogue(@PathVariable("id") Long id) {
        return productServiceClient.getProductById(id);
    }
}