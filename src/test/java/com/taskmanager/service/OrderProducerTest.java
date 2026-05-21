package com.taskmanager.service;

import com.taskmanager.model.Order;
import com.taskmanager.storage.OrderStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderProducer тесты")
class OrderProducerTest {

    @Nested
    @DisplayName("Создание producer")
    class CreationTests {

        @Test
        @DisplayName("Создание producer с параметрами")
        void testProducerCreation() {
            OrderStorage storage = new OrderStorage(10);
            AtomicBoolean running = new AtomicBoolean(true);
            OrderProducer producer = new OrderProducer(storage, running, 5);

            assertNotNull(producer);
            assertEquals(0, producer.getOrdersCreated());
        }
    }

    @Nested
    @DisplayName("Остановка producer")
    class StopTests {

        @Test
        @DisplayName("Остановка producer через stop()")
        void testProducerStop() {
            OrderStorage storage = new OrderStorage(10);
            AtomicBoolean running = new AtomicBoolean(true);
            OrderProducer producer = new OrderProducer(storage, running, 100);

            producer.stop();
            assertFalse(running.get());
        }
    }

    @Nested
    @DisplayName("Запуск producer")
    class RunTests {

        @Test
        @DisplayName("Producer создает заказы")
        void testProducerRun() throws InterruptedException {
            OrderStorage storage = new OrderStorage(10);
            AtomicBoolean running = new AtomicBoolean(true);
            OrderProducer producer = new OrderProducer(storage, running, 3);

            Thread producerThread = new Thread(producer);
            producerThread.start();

            Thread.sleep(2000);
            producer.stop();
            producerThread.interrupt();

            assertTrue(producer.getOrdersCreated() > 0);
        }

        @Test
        @DisplayName("Producer при заполненной очереди")
        void testProducerQueueFull() throws InterruptedException {
            OrderStorage storage = new OrderStorage(1);
            AtomicBoolean running = new AtomicBoolean(true);
            OrderProducer producer = new OrderProducer(storage, running, 5);

            Thread producerThread = new Thread(producer);
            producerThread.start();

            Thread.sleep(1000);
            producer.stop();
            producerThread.interrupt();

            assertTrue(producer.getOrdersCreated() <= 5);
        }
    }
}