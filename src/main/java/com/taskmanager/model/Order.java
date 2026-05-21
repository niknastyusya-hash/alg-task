package com.taskmanager.model;

import com.taskmanager.annotations.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Order {

    public enum Status {
        CREATED,
        PROCESSING,
        COMPLETED,
        FAILED
    }

    @NotNull(message = "Order ID cannot be null")
    @Validate(required = true, message = "Order ID is required")
    private String id;

    @NotNull(message = "Customer name cannot be null")
    @NotEmpty(message = "Customer name cannot be empty")
    @Validate(required = true, message = "Customer name is required")
    private String customerName;

    @NotNull(message = "Product cannot be null")
    @NotEmpty(message = "Product cannot be empty")
    @Validate(required = true, message = "Product is required")
    private String product;

    @OrderType
    private OrderType.Priority priority;

    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private String processedBy;

    public Order() {
        this.id = UUID.randomUUID().toString();
        this.status = Status.CREATED;
        this.createdAt = LocalDateTime.now();
        this.priority = OrderType.Priority.NORMAL;
    }

    public Order(String customerName, String product, OrderType.Priority priority) {
        this();
        this.customerName = customerName;
        this.product = product;
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public OrderType.Priority getPriority() {
        return priority;
    }

    public void setPriority(OrderType.Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public boolean isUrgent() {
        return priority == OrderType.Priority.URGENT;
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
        return String.format("Order{id='%s', customer='%s', product='%s', priority=%s, status=%s}",
                id.substring(0, 8), customerName, product, priority, status);
    }
}
