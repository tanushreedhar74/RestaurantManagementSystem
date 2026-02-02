package com.rms.model;

public class DeliveryAssignment {

    private int assignmentId;
    private int orderId;
    private int deliveryManId;
    private String status; 
    // ASSIGNED, PICKED_UP, OUT_FOR_DELIVERY, DELIVERED

    // 🔹 No-arg constructor
    public DeliveryAssignment() {}

    // 🔹 Constructor for creating assignment
    public DeliveryAssignment(int orderId, int deliveryManId) {
        this.orderId = orderId;
        this.deliveryManId = deliveryManId;
        this.status = "ASSIGNED";
    }

    // 🔹 Getters & Setters
    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getDeliveryManId() {
        return deliveryManId;
    }

    public void setDeliveryManId(int deliveryManId) {
        this.deliveryManId = deliveryManId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}