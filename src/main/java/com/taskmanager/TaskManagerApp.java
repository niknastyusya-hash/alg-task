package com.taskmanager;

import com.taskmanager.model.Order;
import com.taskmanager.service.OrderConsumer;
import com.taskmanager.service.OrderProducer;

import java.util.concurrent.*;
import java.util.logging.Logger;

public class TaskManagerApp {

    private static final Logger logger = Logger.getLogger(TaskManagerApp.class.getName());
    private static final int NUM_CONSUMERS = 3;
    private static final int MAX_ORDERS = 20;
    private static final int QUEUE_CAPACITY = 10;

    public static void main(String[] args) {
        System.out.println("=" .repeat(60));
        System.out.println("ЗАПУСК СИСТЕМЫ ОБРАБОТКИ ЗАКАЗОВ");
        System.out.println("=" .repeat(60));
        System.out.printf("Конфигурация: %d потребителей | %d заказов | Очередь: %d%n%n",
                NUM_CONSUMERS, MAX_ORDERS, QUEUE_CAPACITY);

        BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
        ConcurrentHashMap<String, Order> processedOrders = new ConcurrentHashMap<>();

        OrderProducer producer = new OrderProducer(orderQueue, MAX_ORDERS);

        OrderConsumer[] consumers = new OrderConsumer[NUM_CONSUMERS];
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            consumers[i] = new OrderConsumer(orderQueue, processedOrders, "Потребитель-" + (i + 1));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(NUM_CONSUMERS + 1);

        executorService.submit(producer);

        for (OrderConsumer consumer : consumers) {
            executorService.submit(consumer);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nПолучен сигнал завершения...");
            executorService.shutdownNow();
            producer.stop();
            for (OrderConsumer consumer : consumers) {
                consumer.stop();
            }
        }));

        try {
            executorService.shutdown();

            boolean terminated = executorService.awaitTermination(30, TimeUnit.SECONDS);

            if (!terminated) {
                System.out.println("\nТаймаут ожидания. Принудительное завершение...");
                executorService.shutdownNow();
            }

            producer.stop();
            for (OrderConsumer consumer : consumers) {
                consumer.stop();
            }

            printStatistics(producer, consumers, processedOrders);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Главный поток был прерван");
            executorService.shutdownNow();
        }
    }

    private static void printStatistics(OrderProducer producer,
                                        OrderConsumer[] consumers,
                                        ConcurrentHashMap<String, Order> processedOrders) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("СТАТИСТИКА ОБРАБОТКИ ЗАКАЗОВ");
        System.out.println("=".repeat(60));

        System.out.printf("Создано заказов: %d%n", producer.getOrdersCreated());

        int totalProcessed = 0;
        for (OrderConsumer consumer : consumers) {
            System.out.printf("%s обработал: %d заказов%n",
                    consumer.getClass().getSimpleName().replace("OrderConsumer", "Потребитель"),
                    consumer.getOrdersProcessed());
            totalProcessed += consumer.getOrdersProcessed();
        }

        System.out.printf("Всего обработано: %d заказов%n", totalProcessed);
        System.out.printf("Сохранено в ConcurrentHashMap: %d заказов%n", processedOrders.size());

        long urgentCount = processedOrders.values().stream().filter(Order::isUrgent).count();
        long regularCount = processedOrders.size() - urgentCount;

        System.out.printf("Срочных заказов: %d%n", urgentCount);
        System.out.printf("Обычных заказов: %d%n", regularCount);

        if (!processedOrders.isEmpty()) {
            System.out.println("\nПОСЛЕДНИЕ ОБРАБОТАННЫЕ ЗАКАЗЫ:");
            processedOrders.values().stream()
                    .limit(5)
                    .forEach(order -> System.out.printf("   • %s | %s | %s%n",
                            order.getId().substring(0, 8),
                            order.isUrgent() ? "СРОЧНЫЙ" : " обычный",
                            order.getDescription()));
        }

        System.out.println("\nСистема успешно завершила работу!");
    }
}
