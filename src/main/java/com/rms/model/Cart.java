package com.rms.model;

import java.math.BigDecimal;

public class Cart {

    private int cartId;
    private int customerId;
    private int dishId;
    private String dishName;
    private BigDecimal price;
    private int quantity;

    // 🔹 No-arg constructor
    public Cart() {}

    // 🔹 Constructor for adding to cart
    public Cart(int customerId, int dishId, int quantity) {
        this.customerId = customerId;
        this.dishId = dishId;
        this.quantity = quantity;
    }

    // 🔹 Getters & Setters
    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
