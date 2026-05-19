package com.taskmanager.service;

import com.taskmanager.model.Order;
import com.taskmanager.annotations.OrderType;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class OrderProducer implements Runnable {

    private static final Logger logger = Logger.getLogger(OrderProducer.class.getName());
    private final BlockingQueue<Order> orderQueue;
    private final AtomicInteger ordersCreated;
    private volatile boolean running;
    private final int maxOrders;

    public OrderProducer(BlockingQueue<Order> orderQueue, int maxOrders) {
        this.orderQueue = orderQueue;
        this.maxOrders = maxOrders;
        this.ordersCreated = new AtomicInteger(0);
        this.running = true;
    }

    @Override
    public void run() {
        List<String> descriptions = Arrays.asList(
                "Ноутбук Dell XPS", "Смартфон iPhone 15", "Клавиатура Logitech",
                "Мышь беспроводная", "Монитор Samsung", "Принтер HP",
                "Колонки JBL", "Веб-камера Logitech", "Наушники Sony",
                "Внешний жесткий диск", "USB флешка 64GB", "Чехол для телефона"
        );

        List<String> types = Arrays.asList(
                OrderType.Type.URGENT.name(),
                OrderType.Type.REGULAR.name()
        );

        try {
            while (running && ordersCreated.get() < maxOrders) {
                int orderNum = ordersCreated.incrementAndGet();

                String description = descriptions.get(orderNum % descriptions.size()) + " #" + orderNum;
                String type = types.get(orderNum % types.size());

                Order order = new Order(description, type);

                boolean added = orderQueue.offer(order, 1, java.util.concurrent.TimeUnit.SECONDS);

                if (added) {
                    String priority = order.isUrgent() ? "СРОЧНЫЙ" : "Обычный";
                    logger.info(String.format("[PRODUCER] Создан заказ #%d: %s | Тип: %s | %s",
                            orderNum, order.getId().substring(0, 8), description, priority));
                    System.out.printf(" [ПРОИЗВОДИТЕЛЬ] Создан %s заказ: %s - %s%n",
                            priority, order.getId().substring(0, 8), description);
                } else {
                    ordersCreated.decrementAndGet();
                    logger.warning("[PRODUCER] Не удалось добавить заказ в очередь");
                }

                Thread.sleep(500 + (int)(Math.random() * 500));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warning("[PRODUCER] Производитель прерван");
        }

        logger.info("[PRODUCER] Завершил создание " + ordersCreated.get() + " заказов");
        System.out.println(" Производитель завершил работу. Создано заказов: " + ordersCreated.get());
    }

    public void stop() {
        running = false;
    }

    public int getOrdersCreated() {
        return ordersCreated.get();
    }
}