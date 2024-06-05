package com.app.models;

public class Product {
    private  Long id;
    private  String name;
    private  String type;
    private  String version;
    private  String code;

    public Product(){}

    public Product(Long id, String name, String type, String version, String code) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.version = version;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getVersion() {
        return version;
    }

    public String getCode() {
        return code;
    }
}