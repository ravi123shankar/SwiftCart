package com.swiftcart.model;

public class Product {

    private int id;
    private String name;
    private double price;

    // REQUIRED: No-arg constructor
    public Product() {
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    // Setters (THIS IS WHAT YOU WERE MISSING)
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
