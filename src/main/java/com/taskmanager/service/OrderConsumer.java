package com.taskmanager.service;

import com.taskmanager.model.Order;
import com.taskmanager.storage.OrderStorage;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class OrderConsumer implements Runnable {

    private static final Logger logger = Logger.getLogger(OrderConsumer.class.getName());
    private final OrderStorage storage;
    private final OrderProcessor processor;
    private final AtomicBoolean running;
    private final String consumerName;
    private final AtomicInteger ordersProcessed;

    public OrderConsumer(OrderStorage storage, OrderProcessor processor,
                         AtomicBoolean running, String consumerName) {
        this.storage = storage;
        this.processor = processor;
        this.running = running;
        this.consumerName = consumerName;
        this.ordersProcessed = new AtomicInteger(0);
    }

    @Override
    public void run() {
        try {
            while (running.get()) {
                Order order = storage.pollOrder(2, java.util.concurrent.TimeUnit.SECONDS);

                if (order != null) {
                    boolean success = processor.processOrder(order, consumerName);
                    if (success) {
                        int processed = ordersProcessed.incrementAndGet();
                        String priority = order.isUrgent() ? "СРОЧНЫЙ" : "Обычный";
                        System.out.printf(" %s [%s]: %s - %s (Обработано всего: %d)%n",
                                consumerName, priority, order.getId().substring(0, 8),
                                order.getProduct(), processed);
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.info(String.format("[%s] Consumer interrupted", consumerName));
        }

        logger.info(String.format("[%s] Finished. Processed %d orders",
                consumerName, ordersProcessed.get()));
        System.out.printf("%s завершил работу. Обработано: %d%n",
                consumerName, ordersProcessed.get());
    }

    public void stop() {
        running.set(false);
    }

    public int getOrdersProcessed() {
        return ordersProcessed.get();
    }

    public String getConsumerName() {
        return consumerName;
    }
}
