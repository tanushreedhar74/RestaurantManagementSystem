package com.rms.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Order {

    private int orderId;
    private int customerId;
    private String orderStatus;     // PLACED, APPROVED, PREPARING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
    private BigDecimal totalAmount;
    private String paymentStatus;   // PENDING, PAID
    private Timestamp orderTime;

    // 🔹 No-arg constructor
    public Order() {}

    // 🔹 Constructor for placing order
    public Order(int customerId, BigDecimal totalAmount) {
        this.customerId = customerId;
        this.totalAmount = totalAmount;
        this.orderStatus = "PLACED";
        this.paymentStatus = "PENDING";
    }

    // 🔹 Getters & Setters
    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }
    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }
}