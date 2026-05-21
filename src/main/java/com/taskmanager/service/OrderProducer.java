package com.taskmanager.service;

import com.taskmanager.annotations.OrderType;
import com.taskmanager.model.Order;
import com.taskmanager.storage.OrderStorage;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class OrderProducer implements Runnable {

    private static final Logger logger = Logger.getLogger(OrderProducer.class.getName());
    private final OrderStorage storage;
    private final AtomicBoolean running;
    private final int maxOrders;
    private final AtomicInteger ordersCreated;

    private static final List<String> CUSTOMERS = Arrays.asList(
            "Иван Петров", "Мария Иванова", "Алексей Смирнов",
            "Елена Козлова", "Дмитрий Сидоров", "Анна Васильева"
    );

    private static final List<String> PRODUCTS = Arrays.asList(
            "Ноутбук Dell XPS", "Смартфон iPhone 15", "Клавиатура Logitech",
            "Мышь беспроводная", "Монитор Samsung", "Принтер HP",
            "Колонки JBL", "Наушники Sony", "Внешний жесткий диск"
    );

    public OrderProducer(OrderStorage storage, AtomicBoolean running, int maxOrders) {
        this.storage = storage;
        this.running = running;
        this.maxOrders = maxOrders;
        this.ordersCreated = new AtomicInteger(0);
    }

    @Override
    public void run() {
        try {
            while (running.get() && ordersCreated.get() < maxOrders) {
                int orderNum = ordersCreated.incrementAndGet();

                String customer = CUSTOMERS.get(orderNum % CUSTOMERS.size());
                String product = PRODUCTS.get(orderNum % PRODUCTS.size());
                OrderType.Priority priority = (orderNum % 3 == 0)
                        ? OrderType.Priority.URGENT
                        : OrderType.Priority.NORMAL;

                Order order = new Order(customer, product, priority);

                boolean added = storage.addOrder(order);

                if (added) {
                    String priorityStr = order.isUrgent() ? "СРОЧНЫЙ" : "Обычный";
                    logger.info(String.format("[PRODUCER] Created order #%d: %s | %s | %s",
                            orderNum, order.getId().substring(0, 8), product, priorityStr));
                    System.out.printf(" [ПРОИЗВОДИТЕЛЬ] Создан %s заказ: %s - %s%n",
                            priorityStr, order.getId().substring(0, 8), product);
                } else {
                    ordersCreated.decrementAndGet();
                    logger.warning("[PRODUCER] Failed to add order to queue");
                }

                Thread.sleep(300 + (int)(Math.random() * 300));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warning("[PRODUCER] Producer interrupted");
        }

        logger.info(String.format("[PRODUCER] Finished. Created %d orders", ordersCreated.get()));
        System.out.printf(" Производитель завершил работу. Создано заказов: %d%n", ordersCreated.get());
    }

    public void stop() {
        running.set(false);
    }

    public int getOrdersCreated() {
        return ordersCreated.get();
    }
}

