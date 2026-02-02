package com.rms.model;

public class DeliveryMan {

    private int deliveryManId;
    private String name;
    private String email;
    private String password;
    private String phone;
    private boolean available;

    // 🔹 No-arg constructor
    public DeliveryMan() {}

    // 🔹 Constructor for admin adding delivery man
    public DeliveryMan(String name, String email, String password, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.available = true;
    }

    // 🔹 Getters & Setters
    public int getDeliveryManId() {
        return deliveryManId;
    }

    public void setDeliveryManId(int deliveryManId) {
        this.deliveryManId = deliveryManId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}