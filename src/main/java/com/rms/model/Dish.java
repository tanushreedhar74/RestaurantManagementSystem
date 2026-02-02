package com.rms.model;

import java.math.BigDecimal;

public class Dish {

    private int dishId;
    private String dishName;
    private String cuisine;
    private BigDecimal price;
    private boolean availability;

    // 🔹 No-arg constructor
    public Dish() {}

    // 🔹 Constructor for adding new dish (Admin)
    public Dish(String dishName, String cuisine, BigDecimal price) {
        this.dishName = dishName;
        this.cuisine = cuisine;
        this.price = price;
        this.availability = true;
    }

    // 🔹 Getters & Setters
    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }
}