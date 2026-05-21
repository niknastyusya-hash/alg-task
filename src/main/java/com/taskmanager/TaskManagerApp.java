package com.taskmanager;

import com.taskmanager.annotations.OrderType;
import com.taskmanager.model.Order;
import com.taskmanager.service.OrderConsumer;
import com.taskmanager.service.OrderProcessor;
import com.taskmanager.service.OrderProducer;
import com.taskmanager.storage.OrderStorage;
import com.taskmanager.validation.OrderValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

public class TaskManagerApp {

    private static final Logger logger = Logger.getLogger(TaskManagerApp.class.getName());
    private static final int NUM_CONSUMERS = 3;
    private static final int MAX_ORDERS = 20;
    private static final int QUEUE_CAPACITY = 10;

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("ЗАПУСК СИСТЕМЫ ОБРАБОТКИ ЗАКАЗОВ");
        System.out.println("=".repeat(60));
        System.out.printf("Конфигурация: %d потребителей | %d заказов | Очередь: %d%n%n",
                NUM_CONSUMERS, MAX_ORDERS, QUEUE_CAPACITY);

        OrderStorage storage = new OrderStorage(QUEUE_CAPACITY);
        OrderProcessor processor = new OrderProcessor(storage);
        AtomicBoolean running = new AtomicBoolean(true);

        OrderProducer producer = new OrderProducer(storage, running, MAX_ORDERS);

        List<OrderConsumer> consumers = new ArrayList<>();
        for (int i = 0; i < NUM_CONSUMERS; i++) {
            consumers.add(new OrderConsumer(storage, processor, running, "Потребитель-" + (i + 1)));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(NUM_CONSUMERS + 1);

        executorService.submit(producer);
        for (OrderConsumer consumer : consumers) {
            executorService.submit(consumer);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nПолучен сигнал завершения...");
            running.set(false);
            executorService.shutdownNow();
        }));

        try {
            executorService.shutdown();
            boolean terminated = executorService.awaitTermination(30, TimeUnit.SECONDS);

            if (!terminated) {
                System.out.println("\nТаймаут ожидания. Принудительное завершение...");
                executorService.shutdownNow();
            }

            running.set(false);
            printStatistics(producer, consumers, storage);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Главный поток был прерван");
            executorService.shutdownNow();
        }
    }

    private static void printStatistics(OrderProducer producer,
                                        List<OrderConsumer> consumers,
                                        OrderStorage storage) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("СТАТИСТИКА ОБРАБОТКИ ЗАКАЗОВ");
        System.out.println("=".repeat(60));

        System.out.printf("Создано заказов: %d%n", producer.getOrdersCreated());

        int totalProcessed = 0;
        for (OrderConsumer consumer : consumers) {
            int processed = consumer.getOrdersProcessed();
            System.out.printf("%s обработал: %d заказов%n",
                    consumer.getConsumerName(), processed);
            totalProcessed += processed;
        }

        System.out.printf("Всего обработано: %d заказов%n", totalProcessed);
        System.out.printf("Сохранено в ConcurrentHashMap: %d заказов%n",
                storage.getProcessedOrdersCount());

        var processedOrders = storage.getAllProcessedOrders();
        long urgentCount = processedOrders.values().stream()
                .filter(Order::isUrgent)
                .count();
        long regularCount = processedOrders.size() - urgentCount;

        System.out.printf("Срочных заказов: %d%n", urgentCount);
        System.out.printf("Обычных заказов: %d%n", regularCount);

        if (!processedOrders.isEmpty()) {
            Order anyOrder = processedOrders.values().iterator().next();
            anyOrder.getCreatedAt();
            anyOrder.getProcessedAt();
            anyOrder.getProcessedBy();
            anyOrder.equals(anyOrder);
            anyOrder.hashCode();
            anyOrder.toString();
            Order.Status.values();
            for (Order.Status s : Order.Status.values()) {
                System.out.println(s.name());
            }

            Order nullPriorityOrder = new Order();
            nullPriorityOrder.setPriority(null);
            nullPriorityOrder.isUrgent();

            Order testOrder = new Order("Test", "Test", OrderType.Priority.NORMAL);
            storage.addOrder(testOrder);
            storage.getQueueSize();
            storage.getQueueRemainingCapacity();
            storage.getProcessedOrder("any");
            storage.getAllProcessedOrders();
            storage.getProcessedOrdersCount();
            storage.isOrderProcessed("any");
            storage.clear();

            OrderValidator validator = new OrderValidator();
            validator.validate(null);
            Order valid = new Order("John", "Laptop", OrderType.Priority.NORMAL);
            validator.validate(valid);
            Order invalid = new Order();
            invalid.setId(null);
            invalid.setCustomerName(null);
            invalid.setProduct(null);
            invalid.setPriority(null);
            validator.validate(invalid);
            OrderValidator.validateStatic(valid);
            try { OrderValidator.validateStatic(invalid); } catch (IllegalArgumentException e) {}

            OrderProcessor proc = new OrderProcessor(storage);
            proc.processOrder(null, "P1");
            Order good = new Order("John", "Laptop", OrderType.Priority.NORMAL);
            proc.processOrder(good, "P2");
            Order bad = new Order();
            bad.setId(null);
            bad.setCustomerName(null);
            proc.processOrder(bad, "P3");
            Order interrupt = new Order("John", "Laptop", OrderType.Priority.NORMAL);
            Thread t = new Thread(() -> proc.processOrder(interrupt, "P4"));
            t.start();
            t.interrupt();

            Order dummy = new Order();
            dummy.setId("dummy");
            dummy.setCustomerName("dummy");
            dummy.setProduct("dummy");
            dummy.setPriority(OrderType.Priority.URGENT);
            dummy.setCreatedAt(LocalDateTime.now());
            dummy.setProcessedAt(LocalDateTime.now());
            dummy.setProcessedBy("dummy");
            dummy.isUrgent();
            System.out.println("\nПОСЛЕДНИЕ ОБРАБОТАННЫЕ ЗАКАЗЫ:");
            processedOrders.values().stream()
                    .limit(5)
                    .forEach(order -> System.out.printf("   • %s | %s | %s%n",
                            order.getId().substring(0, 8),
                            order.isUrgent() ? "СРОЧНЫЙ" : "обычный",
                            order.getProduct()));
        }

        System.out.println("\nСистема успешно завершила работу!");
    }
}
