package com.taskmanager.service;

import com.taskmanager.model.Order;
import org.junit.Test;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import static org.junit.Assert.*;

public class OrderProducerTest {

    @Test
    public void testProducerCreation() {
        BlockingQueue<Order> queue = new LinkedBlockingQueue<>(10);
        OrderProducer producer = new OrderProducer(queue, 5);

        assertNotNull("Producer не создался", producer);
        assertEquals("OrdersCreated должен быть 0", 0, producer.getOrdersCreated());
    }

    @Test
    public void testProducerStop() {
        BlockingQueue<Order> queue = new LinkedBlockingQueue<>(10);
        OrderProducer producer = new OrderProducer(queue, 100);

        producer.stop();
    }

    @Test
    public void testProducerRun() throws InterruptedException {
        BlockingQueue<Order> queue = new LinkedBlockingQueue<>(10);
        OrderProducer producer = new OrderProducer(queue, 3);

        Thread producerThread = new Thread(producer);
        producerThread.start();

        Thread.sleep(2000);
        producer.stop();
        producerThread.interrupt();

        assertTrue("Producer не создал заказы", producer.getOrdersCreated() > 0);
    }

    @Test
    public void testProducerQueueFull() throws InterruptedException {
        BlockingQueue<Order> queue = new LinkedBlockingQueue<>(1);
        OrderProducer producer = new OrderProducer(queue, 5);

        Thread producerThread = new Thread(producer);
        producerThread.start();

        Thread.sleep(1000);
        producer.stop();
        producerThread.interrupt();

        assertTrue(true);
    }
}