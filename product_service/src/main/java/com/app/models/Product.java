package com.app.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private final Long id;
    private final String name;
    private final String type;
    private final String version;
    private final String code;
}