package com.taskmanager.model;

import com.taskmanager.annotations.NotNull;
import com.taskmanager.annotations.NotEmpty;
import com.taskmanager.annotations.OrderType;
import com.taskmanager.annotations.Validate;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Order {

    @NotNull(message = "Order ID cannot be null")
    @Validate(required = true, message = "Order ID cannot be null")
    private String id;

    @NotEmpty(message = "Order description cannot be null or empty")
    @Validate(required = true, message = "Order description cannot be null or empty")
    private String description;

    @OrderType
    private String type;

    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private String processedBy;
    private boolean isProcessed;

    public Order() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.isProcessed = false;
    }

    public Order(String description, String type) {
        this();
        this.description = description;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }

    public String getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(String processedBy) {
        this.processedBy = processedBy;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public boolean isUrgent() {
        return OrderType.Type.URGENT.name().equalsIgnoreCase(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Order{id='%s', description='%s', type='%s', createdAt=%s, processed=%s}",
                id, description, type, createdAt, isProcessed);
    }
}