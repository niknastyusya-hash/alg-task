package com.taskmanager.service;

import com.taskmanager.model.Order;
import com.taskmanager.validation.OrderValidator;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class OrderConsumer implements Runnable {

    private static final Logger logger = Logger.getLogger(OrderConsumer.class.getName());
    private final BlockingQueue<Order> orderQueue;
    private final ConcurrentHashMap<String, Order> processedOrders;
    private final AtomicInteger ordersProcessed;
    private final String consumerName;
    private volatile boolean running;
    private final OrderValidator validator;

    public OrderConsumer(BlockingQueue<Order> orderQueue,
                         ConcurrentHashMap<String, Order> processedOrders,
                         String consumerName) {
        this.orderQueue = orderQueue;
        this.processedOrders = processedOrders;
        this.consumerName = consumerName;
        this.ordersProcessed = new AtomicInteger(0);
        this.running = true;
        this.validator = new OrderValidator();
    }

    @Override
    public void run() {
        try {
            while (running) {
                Order order = orderQueue.poll(2, java.util.concurrent.TimeUnit.SECONDS);

                if (order != null) {
                    processOrder(order);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.info("[" + consumerName + "] Поток прерван");
        }

        logger.info("[" + consumerName + "] Завершил работу. Обработано заказов: " + ordersProcessed.get());
        System.out.println(consumerName + " завершил работу. Обработано: " + ordersProcessed.get());
    }

    private void processOrder(Order order) {
        try {
            if (!validator.validate(order)) {
                logger.warning("[" + consumerName + "] Заказ " + order.getId() + " не прошел валидацию");
                System.out.printf("%s: Заказ %s НЕ ПРОШЕЛ валидацию!%n",
                        consumerName, order.getId().substring(0, 8));
                return;
            }

            int processingTime = order.isUrgent() ? 500 : 1000;
            Thread.sleep(processingTime);

            order.setProcessed(true);
            order.setProcessedAt(LocalDateTime.now());
            order.setProcessedBy(consumerName);

            processedOrders.put(order.getId(), order);
            int processed = ordersProcessed.incrementAndGet();

            String priority = order.isUrgent() ? " СРОЧНЫЙ" : " Обычный";
            logger.info(String.format("[%s] Обработан заказ: %s | %s | Время: %dms",
                    consumerName, order.getId().substring(0, 8),
                    order.getDescription(), processingTime));

            System.out.printf(" %s [%s]: %s - %s (Обработано всего: %d)%n",
                    consumerName, priority, order.getId().substring(0, 8),
                    order.getDescription(), processed);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warning("[" + consumerName + "] Прерван при обработке заказа " + order.getId());
        } catch (Exception e) {
            logger.severe("[" + consumerName + "] Ошибка при обработке заказа: " + e.getMessage());
        }
    }

    public void stop() {
        running = false;
    }

    public int getOrdersProcessed() {
        return ordersProcessed.get();
    }
}
