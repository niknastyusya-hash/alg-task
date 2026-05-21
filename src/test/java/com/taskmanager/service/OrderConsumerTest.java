package com.taskmanager.service;

import com.taskmanager.annotations.OrderType;
import com.taskmanager.model.Order;
import com.taskmanager.storage.OrderStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderConsumer тесты")
class OrderConsumerTest {

    @Nested
    @DisplayName("Создание consumer")
    class CreationTests {

        @Test
        @DisplayName("Создание consumer с параметрами")
        void testConsumerCreation() {
            OrderStorage storage = new OrderStorage(10);
            OrderProcessor processor = new OrderProcessor(storage);
            AtomicBoolean running = new AtomicBoolean(true);
            OrderConsumer consumer = new OrderConsumer(storage, processor, running, "ТестовыйConsumer");

            assertNotNull(consumer);
            assertEquals(0, consumer.getOrdersProcessed());
            assertEquals("ТестовыйConsumer", consumer.getConsumerName());
        }
    }

    @Nested
    @DisplayName("Остановка consumer")
    class StopTests {

        @Test
        @DisplayName("Остановка consumer через stop()")
        void testConsumerStop() {
            OrderStorage storage = new OrderStorage(10);
            OrderProcessor processor = new OrderProcessor(storage);
            AtomicBoolean running = new AtomicBoolean(true);
            OrderConsumer consumer = new OrderConsumer(storage, processor, running, "Тестер");

            consumer.stop();
            assertFalse(running.get());
        }
    }

    @Nested
    @DisplayName("Обработка заказов")
    class ProcessingTests {

        @Test
        @DisplayName("Consumer обрабатывает валидный заказ")
        void testConsumerProcessValidOrder() throws InterruptedException {
            OrderStorage storage = new OrderStorage(10);
            OrderProcessor processor = new OrderProcessor(storage);
            AtomicBoolean running = new AtomicBoolean(true);
            OrderConsumer consumer = new OrderConsumer(storage, processor, running, "Тестер");

            Order order = new Order("John", "Laptop", OrderType.Priority.NORMAL);
            storage.addOrder(order);

            Thread consumerThread = new Thread(consumer);
            consumerThread.start();

            Thread.sleep(2000);
            consumer.stop();
            consumerThread.interrupt();

            assertTrue(storage.getProcessedOrdersCount() > 0);
        }

        @Test
        @DisplayName("Consumer обрабатывает невалидный заказ")
        void testConsumerProcessInvalidOrder() throws InterruptedException {
            OrderStorage storage = new OrderStorage(10);
            OrderProcessor processor = new OrderProcessor(storage);
            AtomicBoolean running = new AtomicBoolean(true);
            OrderConsumer consumer = new OrderConsumer(storage, processor, running, "Тестер");

            Order invalidOrder = new Order();
            invalidOrder.setId(null);
            invalidOrder.setCustomerName(null);
            storage.addOrder(invalidOrder);

            Thread consumerThread = new Thread(consumer);
            consumerThread.start();

            Thread.sleep(1000);
            consumer.stop();
            consumerThread.interrupt();

            assertEquals(0, storage.getProcessedOrdersCount());
        }
    }
}