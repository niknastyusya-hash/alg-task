package com.taskmanager.storage;

import com.taskmanager.model.Order;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class OrderStorage {

    private final BlockingQueue<Order> orderQueue;
    private final ConcurrentHashMap<String, Order> processedOrders;
    private final int queueCapacity;

    public OrderStorage(int queueCapacity) {
        this.queueCapacity = queueCapacity;
        this.orderQueue = new LinkedBlockingQueue<>(queueCapacity);
        this.processedOrders = new ConcurrentHashMap<>();
    }

    public boolean addOrder(Order order) {
        return orderQueue.offer(order);
    }

    public Order pollOrder(long timeout, java.util.concurrent.TimeUnit unit) throws InterruptedException {
        return orderQueue.poll(timeout, unit);
    }

    public Order takeOrder() throws InterruptedException {
        return orderQueue.take();
    }

    public int getQueueSize() {
        return orderQueue.size();
    }

    public int getQueueRemainingCapacity() {
        return orderQueue.remainingCapacity();
    }

    public void storeProcessedOrder(Order order) {
        processedOrders.put(order.getId(), order);
    }

    public Order getProcessedOrder(String id) {
        return processedOrders.get(id);
    }

    public ConcurrentHashMap<String, Order> getAllProcessedOrders() {
        return new ConcurrentHashMap<>(processedOrders);
    }

    public int getProcessedOrdersCount() {
        return processedOrders.size();
    }

    public boolean isOrderProcessed(String id) {
        return processedOrders.containsKey(id);
    }

    public void clear() {
        orderQueue.clear();
        processedOrders.clear();
    }
}
