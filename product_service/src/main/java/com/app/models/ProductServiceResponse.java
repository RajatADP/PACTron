package com.app.models;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductServiceResponse {
    private List<Product> products;
}
