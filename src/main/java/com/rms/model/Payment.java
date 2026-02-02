package com.rms.model;

import java.sql.Timestamp;

public class Payment {

    private int paymentId;
    private int orderId;
    private String paymentMode;    // COD / ONLINE
    private String paymentStatus;  // PENDING / PAID
    private Timestamp paidAt;

    // 🔹 No-arg constructor
    public Payment() {}

    // 🔹 Constructor for creating payment record
    public Payment(int orderId, String paymentMode) {
        this.orderId = orderId;
        this.paymentMode = paymentMode;
        this.paymentStatus = "PENDING";
    }

    // 🔹 Getters & Setters
    public int getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getOrderId() {
        return orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }
    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getPaidAt() {
        return paidAt;
    }
    public void setPaidAt(Timestamp paidAt) {
        this.paidAt = paidAt;
    }
}