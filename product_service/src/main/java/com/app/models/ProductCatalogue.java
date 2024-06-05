package com.app.models;

import java.util.List;


public class ProductCatalogue {
    private  List<Product> products;

    public ProductCatalogue(){}

    public ProductCatalogue(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }
}