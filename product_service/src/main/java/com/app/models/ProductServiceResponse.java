package com.app.models;

import java.util.List;

public class ProductServiceResponse {
    private List<Product> products;

    public ProductServiceResponse() {
    }

    public ProductServiceResponse(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }
}
