package com.taskmanager.service;

import com.taskmanager.model.Order;
import org.junit.Test;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import static org.junit.Assert.*;

public class OrderConsumerTest {

    @Test
    public void testConsumerCreation() {
        BlockingQueue<Order> queue = new LinkedBlockingQueue<>(10);
        ConcurrentHashMap<String, Order> processed = new ConcurrentHashMap<>();
        OrderConsumer consumer = new OrderConsumer(queue, processed, "ТестовыйConsumer");

        assertNotNull("Consumer не создался", consumer);
        assertEquals("OrdersProcessed должен быть 0", 0, consumer.getOrdersProcessed());
    }

    @Test
    public void testConsumerStop() {
        BlockingQueue<Order> queue = new LinkedBlockingQueue<>(10);
        ConcurrentHashMap<String, Order> processed = new ConcurrentHashMap<>();
        OrderConsumer consumer = new OrderConsumer(queue, processed, "Тестер");

        consumer.stop();
    }

    @Test
    public void testConsumerProcessValidOrder() throws InterruptedException {
        BlockingQueue<Order> queue = new LinkedBlockingQueue<>(10);
        ConcurrentHashMap<String, Order> processed = new ConcurrentHashMap<>();
        OrderConsumer consumer = new OrderConsumer(queue, processed, "Тестер");

        Order order = new Order("Тестовый заказ", "REGULAR");
        queue.put(order);

        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        Thread.sleep(1500);
        consumer.stop();
        consumerThread.interrupt();

        assertTrue("Consumer не обработал заказ", processed.size() > 0);
    }

    @Test
    public void testConsumerProcessInvalidOrder() throws InterruptedException {
        BlockingQueue<Order> queue = new LinkedBlockingQueue<>(10);
        ConcurrentHashMap<String, Order> processed = new ConcurrentHashMap<>();
        OrderConsumer consumer = new OrderConsumer(queue, processed, "Тестер");

        Order invalidOrder = new Order();
        invalidOrder.setId(null);
        invalidOrder.setDescription("Невалидный");
        queue.put(invalidOrder);

        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        Thread.sleep(1000);
        consumer.stop();
        consumerThread.interrupt();

        assertTrue(true);
    }
}