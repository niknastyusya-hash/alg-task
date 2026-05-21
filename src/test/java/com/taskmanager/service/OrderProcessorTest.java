package com.taskmanager.service;

import com.taskmanager.annotations.OrderType;
import com.taskmanager.model.Order;
import com.taskmanager.storage.OrderStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderProcessor тесты")
class OrderProcessorTest {

    private OrderStorage storage;
    private OrderProcessor processor;

    @BeforeEach
    void setUp() {
        storage = new OrderStorage(10);
        processor = new OrderProcessor(storage);
    }

    @Nested
    @DisplayName("Валидные заказы")
    class ValidOrderTests {

        @Test
        @DisplayName("Обработка NORMAL заказа")
        void testProcessNormalOrder() {
            Order order = new Order("John", "Laptop", OrderType.Priority.NORMAL);
            boolean result = processor.processOrder(order, "Processor");
            assertTrue(result);
            assertEquals(Order.Status.COMPLETED, order.getStatus());
            assertNotNull(order.getProcessedAt());
            assertEquals("Processor", order.getProcessedBy());
        }

        @Test
        @DisplayName("Обработка URGENT заказа")
        void testProcessUrgentOrder() {
            Order order = new Order("John", "Laptop", OrderType.Priority.URGENT);
            boolean result = processor.processOrder(order, "Processor");
            assertTrue(result);
            assertEquals(Order.Status.COMPLETED, order.getStatus());
        }

        @Test
        @DisplayName("Срочный заказ обрабатывается быстрее")
        void testUrgentFaster() {
            Order urgent = new Order("John", "Laptop", OrderType.Priority.URGENT);
            Order normal = new Order("Jane", "Phone", OrderType.Priority.NORMAL);

            long startU = System.currentTimeMillis();
            processor.processOrder(urgent, "P");
            long timeU = System.currentTimeMillis() - startU;

            long startN = System.currentTimeMillis();
            processor.processOrder(normal, "P");
            long timeN = System.currentTimeMillis() - startN;

            assertTrue(timeU < timeN);
        }
    }

    @Nested
    @DisplayName("Невалидные заказы")
    class InvalidOrderTests {

        @Test
        @DisplayName("Null заказ")
        void testNullOrder() {
            boolean result = processor.processOrder(null, "Processor");
            assertFalse(result);
        }

        @Test
        @DisplayName("Заказ с null customerName")
        void testNullCustomerName() {
            Order order = new Order(null, "Product", OrderType.Priority.NORMAL);
            boolean result = processor.processOrder(order, "Processor");
            assertFalse(result);
            assertEquals(Order.Status.FAILED, order.getStatus());
        }

        @Test
        @DisplayName("Заказ с null product")
        void testNullProduct() {
            Order order = new Order("John", null, OrderType.Priority.NORMAL);
            boolean result = processor.processOrder(order, "Processor");
            assertFalse(result);
            assertEquals(Order.Status.FAILED, order.getStatus());
        }

        @Test
        @DisplayName("Заказ с null priority")
        void testNullPriority() {
            Order order = new Order("John", "Product", null);
            boolean result = processor.processOrder(order, "Processor");
            assertFalse(result);
            assertEquals(Order.Status.FAILED, order.getStatus());
        }

        @Test
        @DisplayName("Заказ с пустым customerName")
        void testEmptyCustomerName() {
            Order order = new Order("", "Product", OrderType.Priority.NORMAL);
            boolean result = processor.processOrder(order, "Processor");
            assertFalse(result);
        }

        @Test
        @DisplayName("Заказ с пустым product")
        void testEmptyProduct() {
            Order order = new Order("John", "", OrderType.Priority.NORMAL);
            boolean result = processor.processOrder(order, "Processor");
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("Сохранение в хранилище")
    class StorageTests {

        @Test
        @DisplayName("Успешный заказ сохраняется")
        void testOrderStored() {
            Order order = new Order("John", "Laptop", OrderType.Priority.NORMAL);
            processor.processOrder(order, "P");
            assertTrue(storage.isOrderProcessed(order.getId()));
        }

        @Test
        @DisplayName("Неудачный заказ не сохраняется")
        void testFailedOrderNotStored() {
            Order order = new Order(null, null, OrderType.Priority.NORMAL);
            processor.processOrder(order, "P");
            assertFalse(storage.isOrderProcessed(order.getId()));
        }
    }
}