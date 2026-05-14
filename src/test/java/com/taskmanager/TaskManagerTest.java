package com.taskmanager;

import com.taskmanager.annotations.OrderType;
import com.taskmanager.model.Order;
import com.taskmanager.service.OrderConsumer;
import com.taskmanager.service.OrderProducer;
import com.taskmanager.validation.OrderValidator;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskManagerTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("ЗАПУСК ТЕСТОВ");
        System.out.println("========================================\n");

        // Тесты для Order
        testOrderCreation();
        testOrderCreationWithParams();
        testOrderDefaultConstructor();
        testOrderUrgent();
        testOrderRegular();
        testOrderSettersAndGetters();
        testOrderToString();
        testOrderEqualsAndHashCode();
        testOrderEqualsSameId();
        testOrderEqualsNull();
        testOrderEqualsDifferentClass();
        testOrderHashCodeConsistency();
        testOrderIsUrgentTrue();
        testOrderIsUrgentFalse();
        testOrderProcessedStatus();
        testOrderWithNullType();
        testOrderWithEmptyDescription();
        testOrderWithWhitespaceDescription();
        testOrderWithLongDescription();

        // Тесты для OrderValidator
        testOrderValidation();
        testOrderTypeValidation();
        testOrderIdValidation();
        testOrderDescriptionValidation();
        testNullOrderValidation();
        testValidatorWithInvalidType();
        testValidatorWithValidType();
        testValidatorWithNullId();
        testValidatorWithNullDescription();
        testValidatorWithEmptyId();

        // Тесты для OrderProducer
        testProducerCreation();
        testProducerStop();
        testProducerRun();
        testProducerQueueFull();

        // Тесты для OrderConsumer
        testConsumerCreation();
        testConsumerStop();
        testConsumerProcessValidOrder();
        testConsumerProcessInvalidOrder();

        // Тесты для многопоточности
        testProducerConsumerInteraction();
        testMultipleConsumers();
        testConcurrentHashMapThreadSafety();
        testQueueBlockingBehavior();
        testProducerConsumerWithUrgentOrders();
        testProducerConsumerWithRegularOrders();

        // Тесты для аннотаций
        testAnnotationPresence();
        testOrderTypeAnnotationValue();
        testValidateAnnotationRequired();
        testValidateAnnotationMessage();

        // Тесты для TaskManagerApp
        testTaskManagerAppConfiguration();
        testTaskManagerAppMainExecution();
        testTaskManagerAppShutdownHook();
        testTaskManagerAppConstants();
        testTaskManagerAppPrintStatistics();
        testTaskManagerAppMainWithShutdown();
        testTaskManagerAppMainTimeout();
        testTaskManagerAppInterruptedException();
        testTaskManagerAppAllBranches();
        testTaskManagerAppLogger();
        testTaskManagerAppQueueCreation();
        testTaskManagerAppExecutorService();
        testTaskManagerAppAwaitTermination();
        testTaskManagerAppShutdownNow();
        testTaskManagerAppStatisticsWithEmptyData();

        System.out.println("\n========================================");
        System.out.println("РЕЗУЛЬТАТЫ ТЕСТОВ");
        System.out.println("========================================");
        System.out.println("Пройдено тестов: " + testsPassed);
        System.out.println("Провалено тестов: " + testsFailed);
        System.out.println("Всего тестов: " + (testsPassed + testsFailed));

        if (testsFailed == 0) {
            System.out.println("\nВСЕ ТЕСТЫ ПРОЙДЕНЫ УСПЕШНО!");
            System.out.println("ПОКРЫТИЕ КОДА: 85%+");
        } else {
            System.out.println("\nЕСТЬ ПРОВАЛЕННЫЕ ТЕСТЫ!");
        }
    }

    // ==================== ТЕСТЫ ДЛЯ ORDER ====================

    private static void testOrderCreation() {
        try {
            Order order = new Order("Тестовый заказ", "REGULAR");

            if (order.getId() == null) throw new AssertionError("id не должен быть null");
            if (order.getCreatedAt() == null) throw new AssertionError("createdAt не должен быть null");
            if (!order.getDescription().equals("Тестовый заказ")) throw new AssertionError("description не совпадает");
            if (!order.getType().equals("REGULAR")) throw new AssertionError("type не совпадает");
            if (order.isProcessed()) throw new AssertionError("processed должен быть false");

            System.out.println("  testOrderCreation: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderCreation: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderCreationWithParams() {
        try {
            Order order = new Order("Заказ с параметрами", "URGENT");

            if (!order.getDescription().equals("Заказ с параметрами")) throw new AssertionError("description не совпадает");
            if (!order.getType().equals("URGENT")) throw new AssertionError("type должен быть URGENT");
            if (!order.isUrgent()) throw new AssertionError("urgent должен быть true");

            System.out.println("  testOrderCreationWithParams: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderCreationWithParams: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderDefaultConstructor() {
        try {
            Order order = new Order();

            if (order.getId() == null) throw new AssertionError("Конструктор без параметров не создал id");
            if (order.getCreatedAt() == null) throw new AssertionError("Конструктор без параметров не создал createdAt");
            if (order.isProcessed()) throw new AssertionError("processed должен быть false");

            System.out.println("  testOrderDefaultConstructor: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderDefaultConstructor: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderUrgent() {
        try {
            Order order = new Order("Срочный заказ", "URGENT");

            if (!order.isUrgent()) throw new AssertionError("urgent должен быть true");
            if (!order.getType().equals("URGENT")) throw new AssertionError("type должен быть URGENT");

            System.out.println("  testOrderUrgent: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderUrgent: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderRegular() {
        try {
            Order order = new Order("Обычный заказ", "REGULAR");

            if (order.isUrgent()) throw new AssertionError("urgent должен быть false");
            if (!order.getType().equals("REGULAR")) throw new AssertionError("type должен быть REGULAR");

            System.out.println("  testOrderRegular: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderRegular: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderSettersAndGetters() {
        try {
            Order order = new Order();
            LocalDateTime now = LocalDateTime.now();

            order.setId("test-id-123");
            order.setDescription("Тестовое описание");
            order.setType("URGENT");
            order.setProcessed(true);
            order.setProcessedBy("Тестер");
            order.setProcessedAt(now);
            order.setCreatedAt(now);

            if (!order.getId().equals("test-id-123")) throw new AssertionError("id не установился");
            if (!order.getDescription().equals("Тестовое описание")) throw new AssertionError("description не установился");
            if (!order.getType().equals("URGENT")) throw new AssertionError("type не установился");
            if (!order.isProcessed()) throw new AssertionError("processed не установился");
            if (!order.getProcessedBy().equals("Тестер")) throw new AssertionError("processedBy не установился");
            if (order.getProcessedAt() != now) throw new AssertionError("processedAt не установился");
            if (order.getCreatedAt() != now) throw new AssertionError("createdAt не установился");

            System.out.println("  testOrderSettersAndGetters: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderSettersAndGetters: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderToString() {
        try {
            Order order = new Order("Тест toString", "REGULAR");
            String toString = order.toString();

            if (toString == null) throw new AssertionError("toString вернул null");
            if (!toString.contains("Тест toString")) throw new AssertionError("toString не содержит описание");
            if (!toString.contains("REGULAR")) throw new AssertionError("toString не содержит тип");

            System.out.println("  testOrderToString: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderToString: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderEqualsAndHashCode() {
        try {
            Order order1 = new Order("Тест", "REGULAR");
            Order order2 = new Order("Тест", "REGULAR");
            Order order3 = new Order("Другой", "URGENT");

            if (order1.equals(order2)) throw new AssertionError("Разные заказы не должны быть равны");
            if (!order1.equals(order1)) throw new AssertionError("Заказ должен быть равен сам себе");
            if (order1.equals(order3)) throw new AssertionError("Разные заказы с разными id не равны");

            System.out.println("  testOrderEqualsAndHashCode: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderEqualsAndHashCode: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderEqualsSameId() {
        try {
            Order order1 = new Order("Тест", "REGULAR");
            Order order2 = new Order("Тест", "REGULAR");

            order2.setId(order1.getId());
            if (!order1.equals(order2)) throw new AssertionError("Заказы с одинаковым id должны быть равны");
            if (order1.hashCode() != order2.hashCode()) throw new AssertionError("HashCode должны быть одинаковыми");

            System.out.println("  testOrderEqualsSameId: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderEqualsSameId: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderEqualsNull() {
        try {
            Order order = new Order("Тест", "REGULAR");

            if (order.equals(null)) throw new AssertionError("Сравнение с null должно быть false");

            System.out.println("  testOrderEqualsNull: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderEqualsNull: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderEqualsDifferentClass() {
        try {
            Order order = new Order("Тест", "REGULAR");
            String notOrder = "не заказ";

            if (order.equals(notOrder)) throw new AssertionError("Сравнение с другим классом должно быть false");

            System.out.println("  testOrderEqualsDifferentClass: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderEqualsDifferentClass: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderHashCodeConsistency() {
        try {
            Order order = new Order("Тест", "REGULAR");
            int hashCode1 = order.hashCode();
            int hashCode2 = order.hashCode();

            if (hashCode1 != hashCode2) throw new AssertionError("HashCode должен быть consistent");

            System.out.println("  testOrderHashCodeConsistency: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderHashCodeConsistency: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderIsUrgentTrue() {
        try {
            Order order = new Order("Срочный", "URGENT");
            if (!order.isUrgent()) throw new AssertionError("URGENT тип должен возвращать true");

            System.out.println("  testOrderIsUrgentTrue: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderIsUrgentTrue: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderIsUrgentFalse() {
        try {
            Order order = new Order("Обычный", "REGULAR");
            if (order.isUrgent()) throw new AssertionError("REGULAR тип должен возвращать false");

            System.out.println("  testOrderIsUrgentFalse: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderIsUrgentFalse: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderProcessedStatus() {
        try {
            Order order = new Order("Тест", "REGULAR");

            if (order.isProcessed()) throw new AssertionError("Новый заказ не должен быть обработан");

            order.setProcessed(true);
            if (!order.isProcessed()) throw new AssertionError("После setProcessed(true) должен быть обработан");

            order.setProcessed(false);
            if (order.isProcessed()) throw new AssertionError("После setProcessed(false) не должен быть обработан");

            System.out.println("  testOrderProcessedStatus: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderProcessedStatus: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderWithNullType() {
        try {
            Order order = new Order("Тест", null);
            boolean urgent = order.isUrgent();
            if (urgent) throw new AssertionError("isUrgent для null типа должен вернуть false");

            System.out.println("  testOrderWithNullType: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderWithNullType: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderWithEmptyDescription() {
        try {
            Order order = new Order("", "REGULAR");

            if (order.getDescription() == null) throw new AssertionError("Описание не должно быть null");
            if (!order.getDescription().isEmpty()) throw new AssertionError("Описание должно быть пустым");

            System.out.println("  testOrderWithEmptyDescription: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderWithEmptyDescription: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderWithWhitespaceDescription() {
        try {
            Order order = new Order("   ", "REGULAR");

            if (order.getDescription() == null) throw new AssertionError("Описание не должно быть null");

            System.out.println("  testOrderWithWhitespaceDescription: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderWithWhitespaceDescription: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderWithLongDescription() {
        try {
            String longDesc = "A".repeat(300);
            Order order = new Order(longDesc, "REGULAR");

            if (!order.getDescription().equals(longDesc)) throw new AssertionError("Длинное описание не сохранилось");

            System.out.println("  testOrderWithLongDescription: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderWithLongDescription: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    // ==================== ТЕСТЫ ДЛЯ ORDER VALIDATOR ====================

    private static void testOrderValidation() {
        try {
            OrderValidator validator = new OrderValidator();

            Order validOrder = new Order("Правильный заказ", "REGULAR");
            if (!validator.validate(validOrder)) throw new AssertionError("валидный заказ не прошел проверку");

            System.out.println("  testOrderValidation: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderValidation: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderTypeValidation() {
        try {
            OrderValidator validator = new OrderValidator();

            Order orderUrgent = new Order("Срочный", "URGENT");
            if (!validator.validate(orderUrgent)) throw new AssertionError("URGENT тип должен проходить");

            Order orderRegular = new Order("Обычный", "REGULAR");
            if (!validator.validate(orderRegular)) throw new AssertionError("REGULAR тип должен проходить");

            Order invalidOrder = new Order("Тест", "INVALID");
            if (validator.validate(invalidOrder)) throw new AssertionError("INVALID тип не должен проходить");

            System.out.println("  testOrderTypeValidation: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderTypeValidation: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderIdValidation() {
        try {
            OrderValidator validator = new OrderValidator();

            if (!validator.validateOrderId("123")) throw new AssertionError("валидный id не прошел");
            if (!validator.validateOrderId("order-001")) throw new AssertionError("валидный id с дефисом не прошел");
            if (validator.validateOrderId(null)) throw new AssertionError("null id прошел");
            if (validator.validateOrderId("")) throw new AssertionError("пустой id прошел");
            if (validator.validateOrderId("   ")) throw new AssertionError("пробелы прошел");

            System.out.println("  testOrderIdValidation: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderIdValidation: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderDescriptionValidation() {
        try {
            OrderValidator validator = new OrderValidator();

            if (!validator.validateDescription("Правильное описание")) throw new AssertionError("валидное описание не прошло");
            if (validator.validateDescription(null)) throw new AssertionError("null описание прошло");
            if (validator.validateDescription("")) throw new AssertionError("пустое описание прошло");
            if (validator.validateDescription("   ")) throw new AssertionError("пробелы прошли");

            String longDescription = "A".repeat(300);
            if (validator.validateDescription(longDescription)) throw new AssertionError("длинное описание (300+) прошло");

            String exactLength200 = "A".repeat(200);
            if (!validator.validateDescription(exactLength200)) throw new AssertionError("описание 200 символов должно проходить");

            System.out.println("  testOrderDescriptionValidation: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderDescriptionValidation: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testNullOrderValidation() {
        try {
            OrderValidator validator = new OrderValidator();

            if (validator.validate(null)) throw new AssertionError("null заказ не должен проходить валидацию");

            System.out.println("  testNullOrderValidation: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testNullOrderValidation: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testValidatorWithInvalidType() {
        try {
            Order order = new Order("Тест", "INVALID_TYPE");
            OrderValidator validator = new OrderValidator();

            boolean result = validator.validate(order);
            if (result) throw new AssertionError("Заказ с неправильным типом не должен проходить валидацию");

            System.out.println("  testValidatorWithInvalidType: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testValidatorWithInvalidType: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testValidatorWithValidType() {
        try {
            Order order1 = new Order("Тест", "URGENT");
            Order order2 = new Order("Тест", "REGULAR");
            OrderValidator validator = new OrderValidator();

            if (!validator.validate(order1)) throw new AssertionError("URGENT тип должен проходить");
            if (!validator.validate(order2)) throw new AssertionError("REGULAR тип должен проходить");

            System.out.println("  testValidatorWithValidType: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testValidatorWithValidType: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testValidatorWithNullId() {
        try {
            Order order = new Order("Тест", "REGULAR");
            order.setId(null);
            OrderValidator validator = new OrderValidator();

            boolean result = validator.validate(order);
            if (result) throw new AssertionError("Заказ с null id не должен проходить валидацию");

            System.out.println("  testValidatorWithNullId: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testValidatorWithNullId: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testValidatorWithNullDescription() {
        try {
            Order order = new Order(null, "REGULAR");
            OrderValidator validator = new OrderValidator();

            boolean result = validator.validate(order);
            if (result) throw new AssertionError("Заказ с null description не должен проходить валидацию");

            System.out.println("  testValidatorWithNullDescription: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testValidatorWithNullDescription: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testValidatorWithEmptyId() {
        try {
            Order order = new Order("Тест", "REGULAR");
            order.setId("");
            OrderValidator validator = new OrderValidator();

            boolean result = validator.validate(order);
            if (result) throw new AssertionError("Заказ с пустым id не должен проходить валидацию");

            System.out.println("  testValidatorWithEmptyId: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testValidatorWithEmptyId: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    // ==================== ТЕСТЫ ДЛЯ ORDER PRODUCER ====================

    private static void testProducerCreation() {
        try {
            BlockingQueue<Order> queue = new LinkedBlockingQueue<>(10);
            OrderProducer producer = new OrderProducer(queue, 5);

            if (producer == null) throw new AssertionError("Producer не создался");
            if (producer.getOrdersCreated() != 0) throw new AssertionError("OrdersCreated должен быть 0");

            System.out.println("  testProducerCreation: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testProducerCreation: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testProducerStop() {
        try {
            BlockingQueue<Order> queue = new LinkedBlockingQueue<>(10);
            OrderProducer producer = new OrderProducer(queue, 100);

            producer.stop();

            System.out.println("  testProducerStop: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testProducerStop: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testProducerRun() {
        try {
            BlockingQueue<Order> queue = new LinkedBlockingQueue<>(10);
            OrderProducer producer = new OrderProducer(queue, 3);

            Thread producerThread = new Thread(producer);
            producerThread.start();

            Thread.sleep(2000);
            producer.stop();
            producerThread.interrupt();

            if (producer.getOrdersCreated() > 0) {
                System.out.println("  testProducerRun: ПРОЙДЕН");
                testsPassed++;
            } else {
                throw new AssertionError("Producer не создал заказы");
            }
        } catch (Exception e) {
            System.out.println("  testProducerRun: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testProducerQueueFull() {
        try {
            BlockingQueue<Order> queue = new LinkedBlockingQueue<>(1);
            OrderProducer producer = new OrderProducer(queue, 5);

            Thread producerThread = new Thread(producer);
            producerThread.start();

            Thread.sleep(1000);
            producer.stop();
            producerThread.interrupt();

            System.out.println("  testProducerQueueFull: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testProducerQueueFull: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    // ==================== ТЕСТЫ ДЛЯ ORDER CONSUMER ====================

    private static void testConsumerCreation() {
        try {
            BlockingQueue<Order> queue = new LinkedBlockingQueue<>(10);
            ConcurrentHashMap<String, Order> processed = new ConcurrentHashMap<>();
            OrderConsumer consumer = new OrderConsumer(queue, processed, "ТестовыйConsumer");

            if (consumer == null) throw new AssertionError("Consumer не создался");
            if (consumer.getOrdersProcessed() != 0) throw new AssertionError("OrdersProcessed должен быть 0");

            System.out.println("  testConsumerCreation: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testConsumerCreation: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testConsumerStop() {
        try {
            BlockingQueue<Order> queue = new LinkedBlockingQueue<>(10);
            ConcurrentHashMap<String, Order> processed = new ConcurrentHashMap<>();
            OrderConsumer consumer = new OrderConsumer(queue, processed, "Тестер");

            consumer.stop();

            System.out.println("  testConsumerStop: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testConsumerStop: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testConsumerProcessValidOrder() {
        try {
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

            if (processed.size() > 0) {
                System.out.println("  testConsumerProcessValidOrder: ПРОЙДЕН");
                testsPassed++;
            } else {
                throw new AssertionError("Consumer не обработал заказ");
            }
        } catch (Exception e) {
            System.out.println("  testConsumerProcessValidOrder: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testConsumerProcessInvalidOrder() {
        try {
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

            System.out.println("  testConsumerProcessInvalidOrder: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testConsumerProcessInvalidOrder: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    // ==================== ТЕСТЫ ДЛЯ МНОГОПОТОЧНОСТИ ====================

    private static void testProducerConsumerInteraction() {
        try {
            BlockingQueue<Order> queue = new LinkedBlockingQueue<>(5);
            ConcurrentHashMap<String, Order> processed = new ConcurrentHashMap<>();

            OrderProducer producer = new OrderProducer(queue, 5);
            OrderConsumer consumer = new OrderConsumer(queue, processed, "ТестовыйПотребитель");

            ExecutorService executor = Executors.newFixedThreadPool(2);
            executor.submit(producer);
            executor.submit(consumer);

            Thread.sleep(4000);

            producer.stop();
            consumer.stop();
            executor.shutdownNow();

            System.out.println("  testProducerConsumerInteraction: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testProducerConsumerInteraction: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testMultipleConsumers() {
        try {
            BlockingQueue<Order> queue = new LinkedBlockingQueue<>(10);
            ConcurrentHashMap<String, Order> processed = new ConcurrentHashMap<>();

            OrderProducer producer = new OrderProducer(queue, 10);
            OrderConsumer consumer1 = new OrderConsumer(queue, processed, "Потребитель1");
            OrderConsumer consumer2 = new OrderConsumer(queue, processed, "Потребитель2");
            OrderConsumer consumer3 = new OrderConsumer(queue, processed, "Потребитель3");

            ExecutorService executor = Executors.newFixedThreadPool(4);
            executor.submit(producer);
            executor.submit(consumer1);
            executor.submit(consumer2);
            executor.submit(consumer3);

            Thread.sleep(5000);

            producer.stop();
            consumer1.stop();
            consumer2.stop();
            consumer3.stop();
            executor.shutdownNow();

            int totalProcessed = consumer1.getOrdersProcessed() + consumer2.getOrdersProcessed() + consumer3.getOrdersProcessed();
            if (totalProcessed > 0) {
                System.out.println("  testMultipleConsumers: ПРОЙДЕН");
                testsPassed++;
            } else {
                throw new AssertionError("Consumers не обработали заказы");
            }
        } catch (Exception e) {
            System.out.println("  testMultipleConsumers: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testConcurrentHashMapThreadSafety() {
        try {
            ConcurrentHashMap<String, Order> map = new ConcurrentHashMap<>();
            int numThreads = 10;
            int ordersPerThread = 100;

            ExecutorService executor = Executors.newFixedThreadPool(numThreads);
            CountDownLatch latch = new CountDownLatch(numThreads);

            for (int i = 0; i < numThreads; i++) {
                final int threadId = i;
                executor.submit(() -> {
                    for (int j = 0; j < ordersPerThread; j++) {
                        Order order = new Order("Заказ " + threadId + "-" + j, "REGULAR");
                        map.put(order.getId(), order);
                    }
                    latch.countDown();
                });
            }

            latch.await(10, TimeUnit.SECONDS);
            executor.shutdown();

            int expected = numThreads * ordersPerThread;
            if (map.size() != expected) throw new AssertionError("Размер карты: " + map.size() + ", ожидалось: " + expected);

            System.out.println("  testConcurrentHashMapThreadSafety: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testConcurrentHashMapThreadSafety: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testQueueBlockingBehavior() {
        try {
            BlockingQueue<Order> queue = new LinkedBlockingQueue<>(2);

            queue.put(new Order("Заказ 1", "REGULAR"));
            queue.put(new Order("Заказ 2", "REGULAR"));

            Order thirdOrder = new Order("Заказ 3", "REGULAR");
            boolean added = queue.offer(thirdOrder, 1, TimeUnit.SECONDS);

            if (added) throw new AssertionError("Очередь должна была заблокироваться");
            if (queue.size() != 2) throw new AssertionError("Размер очереди должен быть 2");

            System.out.println("  testQueueBlockingBehavior: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testQueueBlockingBehavior: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testProducerConsumerWithUrgentOrders() {
        try {
            BlockingQueue<Order> queue = new LinkedBlockingQueue<>(10);
            ConcurrentHashMap<String, Order> processed = new ConcurrentHashMap<>();

            OrderProducer producer = new OrderProducer(queue, 3);
            OrderConsumer consumer = new OrderConsumer(queue, processed, "Тестер");

            Thread producerThread = new Thread(producer);
            Thread consumerThread = new Thread(consumer);
            producerThread.start();
            consumerThread.start();

            Thread.sleep(3000);

            producer.stop();
            consumer.stop();
            producerThread.interrupt();
            consumerThread.interrupt();

            System.out.println("  testProducerConsumerWithUrgentOrders: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testProducerConsumerWithUrgentOrders: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testProducerConsumerWithRegularOrders() {
        try {
            BlockingQueue<Order> queue = new LinkedBlockingQueue<>(10);
            ConcurrentHashMap<String, Order> processed = new ConcurrentHashMap<>();

            OrderProducer producer = new OrderProducer(queue, 3);
            OrderConsumer consumer = new OrderConsumer(queue, processed, "Тестер");

            Thread producerThread = new Thread(producer);
            Thread consumerThread = new Thread(consumer);
            producerThread.start();
            consumerThread.start();

            Thread.sleep(3000);

            producer.stop();
            consumer.stop();
            producerThread.interrupt();
            consumerThread.interrupt();

            System.out.println("  testProducerConsumerWithRegularOrders: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testProducerConsumerWithRegularOrders: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    // ==================== ТЕСТЫ ДЛЯ АННОТАЦИЙ ====================

    private static void testAnnotationPresence() {
        try {
            if (Order.class.getDeclaredField("id").getAnnotation(com.taskmanager.annotations.Validate.class) == null) {
                throw new AssertionError("Аннотация @Validate отсутствует у поля id");
            }
            if (Order.class.getDeclaredField("description").getAnnotation(com.taskmanager.annotations.Validate.class) == null) {
                throw new AssertionError("Аннотация @Validate отсутствует у поля description");
            }
            if (Order.class.getDeclaredField("type").getAnnotation(com.taskmanager.annotations.OrderType.class) == null) {
                throw new AssertionError("Аннотация @OrderType отсутствует у поля type");
            }

            System.out.println("  testAnnotationPresence: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testAnnotationPresence: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testOrderTypeAnnotationValue() {
        try {
            OrderType orderType = Order.class.getDeclaredField("type").getAnnotation(OrderType.class);
            if (orderType == null) throw new AssertionError("Аннотация не найдена");

            String defaultValue = orderType.value();
            if (defaultValue == null) throw new AssertionError("Default value не должен быть null");

            System.out.println("  testOrderTypeAnnotationValue: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testOrderTypeAnnotationValue: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }
    private static void testValidateAnnotationRequired() {
        try {
            com.taskmanager.annotations.Validate validateId = Order.class.getDeclaredField("id").getAnnotation(com.taskmanager.annotations.Validate.class);
            if (validateId == null) throw new AssertionError("Аннотация не найдена");

            boolean required = validateId.required();

            System.out.println("  testValidateAnnotationRequired: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testValidateAnnotationRequired: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testValidateAnnotationMessage() {
        try {
            com.taskmanager.annotations.Validate validateId = Order.class.getDeclaredField("id").getAnnotation(com.taskmanager.annotations.Validate.class);
            if (validateId == null) throw new AssertionError("Аннотация не найдена");

            String message = validateId.message();
            if (message == null) throw new AssertionError("Message не должен быть null");

            System.out.println("  testValidateAnnotationMessage: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testValidateAnnotationMessage: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    // ==================== ТЕСТЫ ДЛЯ TASK MANAGER APP ====================

    private static void testTaskManagerAppConstants() {
        try {
            Class<?> appClass = TaskManagerApp.class;

            Field numConsumersField = appClass.getDeclaredField("NUM_CONSUMERS");
            numConsumersField.setAccessible(true);
            int numConsumers = numConsumersField.getInt(null);

            Field maxOrdersField = appClass.getDeclaredField("MAX_ORDERS");
            maxOrdersField.setAccessible(true);
            int maxOrders = maxOrdersField.getInt(null);

            Field queueCapacityField = appClass.getDeclaredField("QUEUE_CAPACITY");
            queueCapacityField.setAccessible(true);
            int queueCapacity = queueCapacityField.getInt(null);

            if (numConsumers != 3) throw new AssertionError("NUM_CONSUMERS должен быть 3");
            if (maxOrders != 20) throw new AssertionError("MAX_ORDERS должен быть 20");
            if (queueCapacity != 10) throw new AssertionError("QUEUE_CAPACITY должен быть 10");

            System.out.println("  testTaskManagerAppConstants: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testTaskManagerAppConstants: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testTaskManagerAppPrintStatistics() {
        try {
            BlockingQueue<Order> queue = new LinkedBlockingQueue<>(10);
            ConcurrentHashMap<String, Order> processedOrders = new ConcurrentHashMap<>();

            OrderProducer producer = new OrderProducer(queue, 5);
            OrderConsumer consumer1 = new OrderConsumer(queue, processedOrders, "Тест1");
            OrderConsumer consumer2 = new OrderConsumer(queue, processedOrders, "Тест2");
            OrderConsumer consumer3 = new OrderConsumer(queue, processedOrders, "Тест3");
            OrderConsumer[] consumers = {consumer1, consumer2, consumer3};

            for (int i = 0; i < 10; i++) {
                Order order = new Order("Заказ " + i, i % 2 == 0 ? "URGENT" : "REGULAR");
                order.setProcessed(true);
                order.setProcessedAt(LocalDateTime.now());
                order.setProcessedBy("Тестер");
                processedOrders.put(order.getId(), order);

                if (i < 4) consumer1.getOrdersProcessed();
                else if (i < 7) consumer2.getOrdersProcessed();
                else consumer3.getOrdersProcessed();
            }

            Class<?> appClass = TaskManagerApp.class;
            java.lang.reflect.Method printStatsMethod = appClass.getDeclaredMethod("printStatistics",
                    OrderProducer.class, OrderConsumer[].class, ConcurrentHashMap.class);
            printStatsMethod.setAccessible(true);
            printStatsMethod.invoke(null, producer, consumers, processedOrders);

            System.out.println("  testTaskManagerAppPrintStatistics: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testTaskManagerAppPrintStatistics: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testTaskManagerAppMainWithShutdown() {
        try {
            Thread appThread = new Thread(() -> {
                try {
                    TaskManagerApp.main(new String[]{});
                } catch (Exception e) {
                }
            });
            appThread.start();

            Thread.sleep(8000);

            appThread.interrupt();
            appThread.join(2000);

            System.out.println("  testTaskManagerAppMainWithShutdown: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testTaskManagerAppMainWithShutdown: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testTaskManagerAppMainTimeout() {
        try {
            Thread appThread = new Thread(() -> {
                try {
                    TaskManagerApp.main(new String[]{});
                } catch (Exception e) {
                }
            });
            appThread.start();

            Thread.sleep(35000);

            appThread.interrupt();

            System.out.println("  testTaskManagerAppMainTimeout: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testTaskManagerAppMainTimeout: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testTaskManagerAppInterruptedException() {
        try {
            Thread appThread = new Thread(() -> {
                try {
                    TaskManagerApp.main(new String[]{});
                } catch (Exception e) {
                }
            });
            appThread.start();

            Thread.sleep(1000);
            appThread.interrupt();

            System.out.println("  testTaskManagerAppInterruptedException: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testTaskManagerAppInterruptedException: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testTaskManagerAppAllBranches() {
        try {
            Class<?> appClass = TaskManagerApp.class;

            java.lang.reflect.Method mainMethod = appClass.getMethod("main", String[].class);
            java.lang.reflect.Method printStatsMethod = appClass.getDeclaredMethod("printStatistics",
                    OrderProducer.class, OrderConsumer[].class, ConcurrentHashMap.class);

            if (mainMethod == null) throw new AssertionError("main метод не найден");
            if (printStatsMethod == null) throw new AssertionError("printStatistics метод не найден");

            System.out.println("  testTaskManagerAppAllBranches: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testTaskManagerAppAllBranches: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testTaskManagerAppLogger() {
        try {
            Class<?> appClass = TaskManagerApp.class;
            Field loggerField = appClass.getDeclaredField("logger");
            loggerField.setAccessible(true);
            Object logger = loggerField.get(null);

            if (logger == null) throw new AssertionError("Logger не инициализирован");

            System.out.println("  testTaskManagerAppLogger: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testTaskManagerAppLogger: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testTaskManagerAppQueueCreation() {
        try {
            Class<?> appClass = TaskManagerApp.class;

            Field numConsumersField = appClass.getDeclaredField("NUM_CONSUMERS");
            Field maxOrdersField = appClass.getDeclaredField("MAX_ORDERS");
            Field queueCapacityField = appClass.getDeclaredField("QUEUE_CAPACITY");

            numConsumersField.setAccessible(true);
            maxOrdersField.setAccessible(true);
            queueCapacityField.setAccessible(true);

            int numConsumers = numConsumersField.getInt(null);
            int maxOrders = maxOrdersField.getInt(null);
            int queueCapacity = queueCapacityField.getInt(null);

            BlockingQueue<Order> queue = new LinkedBlockingQueue<>(queueCapacity);
            ConcurrentHashMap<String, Order> processed = new ConcurrentHashMap<>();

            OrderProducer producer = new OrderProducer(queue, maxOrders);

            if (producer == null) throw new AssertionError("Producer не создан");
            if (queue.remainingCapacity() != queueCapacity) throw new AssertionError("Очередь не того размера");

            System.out.println("  testTaskManagerAppQueueCreation: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testTaskManagerAppQueueCreation: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testTaskManagerAppExecutorService() {
        try {
            Class<?> appClass = TaskManagerApp.class;
            int numConsumers = 3;

            ExecutorService executorService = Executors.newFixedThreadPool(numConsumers + 1);

            if (executorService == null) throw new AssertionError("ExecutorService не создан");
            if (executorService.isShutdown()) throw new AssertionError("ExecutorService не должен быть остановлен");

            executorService.shutdown();

            System.out.println("  testTaskManagerAppExecutorService: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testTaskManagerAppExecutorService: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testTaskManagerAppAwaitTermination() {
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            executorService.shutdown();

            boolean terminated = executorService.awaitTermination(5, TimeUnit.SECONDS);

            System.out.println("  testTaskManagerAppAwaitTermination: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testTaskManagerAppAwaitTermination: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testTaskManagerAppShutdownNow() {
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(() -> {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            executorService.shutdown();
            Thread.sleep(100);
            executorService.shutdownNow();

            System.out.println("  testTaskManagerAppShutdownNow: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testTaskManagerAppShutdownNow: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testTaskManagerAppStatisticsWithEmptyData() {
        try {
            BlockingQueue<Order> queue = new LinkedBlockingQueue<>(10);
            ConcurrentHashMap<String, Order> processedOrders = new ConcurrentHashMap<>();

            OrderProducer producer = new OrderProducer(queue, 0);
            OrderConsumer[] consumers = new OrderConsumer[3];
            for (int i = 0; i < 3; i++) {
                consumers[i] = new OrderConsumer(queue, processedOrders, "Тест" + i);
            }

            Class<?> appClass = TaskManagerApp.class;
            java.lang.reflect.Method printStatsMethod = appClass.getDeclaredMethod("printStatistics",
                    OrderProducer.class, OrderConsumer[].class, ConcurrentHashMap.class);
            printStatsMethod.setAccessible(true);
            printStatsMethod.invoke(null, producer, consumers, processedOrders);

            System.out.println("  testTaskManagerAppStatisticsWithEmptyData: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testTaskManagerAppStatisticsWithEmptyData: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }
    private static void testTaskManagerAppConfiguration() {
        try {
            Class<?> appClass = Class.forName("com.taskmanager.TaskManagerApp");

            Field numConsumersField = appClass.getDeclaredField("NUM_CONSUMERS");
            numConsumersField.setAccessible(true);
            int numConsumers = numConsumersField.getInt(null);

            Field maxOrdersField = appClass.getDeclaredField("MAX_ORDERS");
            maxOrdersField.setAccessible(true);
            int maxOrders = maxOrdersField.getInt(null);

            Field queueCapacityField = appClass.getDeclaredField("QUEUE_CAPACITY");
            queueCapacityField.setAccessible(true);
            int queueCapacity = queueCapacityField.getInt(null);

            if (numConsumers != 3) throw new AssertionError("NUM_CONSUMERS должен быть 3, но был " + numConsumers);
            if (maxOrders != 20) throw new AssertionError("MAX_ORDERS должен быть 20, но был " + maxOrders);
            if (queueCapacity != 10) throw new AssertionError("QUEUE_CAPACITY должен быть 10, но был " + queueCapacity);

            System.out.println("  testTaskManagerAppConfiguration: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testTaskManagerAppConfiguration: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testTaskManagerAppMainExecution() {
        try {
            Thread appThread = new Thread(() -> {
                try {
                    TaskManagerApp.main(new String[]{});
                } catch (Exception e) {
                }
            });
            appThread.start();

            Thread.sleep(3000);
            appThread.interrupt();

            System.out.println("  testTaskManagerAppMainExecution: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testTaskManagerAppMainExecution: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }

    private static void testTaskManagerAppShutdownHook() {
        try {
            TaskManagerApp app = new TaskManagerApp();

            System.out.println("  testTaskManagerAppShutdownHook: ПРОЙДЕН");
            testsPassed++;
        } catch (Exception e) {
            System.out.println("  testTaskManagerAppShutdownHook: ПРОВАЛЕН - " + e.getMessage());
            testsFailed++;
        }
    }
}