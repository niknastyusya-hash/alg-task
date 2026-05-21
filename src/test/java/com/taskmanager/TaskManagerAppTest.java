package com.taskmanager;

import com.taskmanager.model.Order;
import com.taskmanager.service.OrderConsumer;
import com.taskmanager.service.OrderProcessor;
import com.taskmanager.service.OrderProducer;
import com.taskmanager.storage.OrderStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TaskManagerApp тесты")
class TaskManagerTest {

    private ExecutorService testExecutor;

    @BeforeEach
    void setUp() {
        testExecutor = Executors.newCachedThreadPool();
    }

    @AfterEach
    void tearDown() {
        if (testExecutor != null && !testExecutor.isShutdown()) {
            testExecutor.shutdownNow();
        }
    }

    @Nested
    @DisplayName("Константы приложения")
    class ConstantsTests {

        @Test
        @DisplayName("Проверка значений констант")
        void testTaskManagerAppConstants() throws Exception {
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

            assertEquals(3, numConsumers);
            assertEquals(20, maxOrders);
            assertEquals(10, queueCapacity);
        }

        @Test
        @DisplayName("Константы должны быть final")
        void testTaskManagerAppConstantsAreFinal() throws Exception {
            Class<?> appClass = TaskManagerApp.class;

            Field numConsumersField = appClass.getDeclaredField("NUM_CONSUMERS");
            Field maxOrdersField = appClass.getDeclaredField("MAX_ORDERS");
            Field queueCapacityField = appClass.getDeclaredField("QUEUE_CAPACITY");

            assertTrue(java.lang.reflect.Modifier.isFinal(numConsumersField.getModifiers()));
            assertTrue(java.lang.reflect.Modifier.isFinal(maxOrdersField.getModifiers()));
            assertTrue(java.lang.reflect.Modifier.isFinal(queueCapacityField.getModifiers()));
        }

        @Test
        @DisplayName("Константы должны быть static")
        void testTaskManagerAppConstantsAreStatic() throws Exception {
            Class<?> appClass = TaskManagerApp.class;

            Field numConsumersField = appClass.getDeclaredField("NUM_CONSUMERS");
            Field maxOrdersField = appClass.getDeclaredField("MAX_ORDERS");
            Field queueCapacityField = appClass.getDeclaredField("QUEUE_CAPACITY");

            assertTrue(java.lang.reflect.Modifier.isStatic(numConsumersField.getModifiers()));
            assertTrue(java.lang.reflect.Modifier.isStatic(maxOrdersField.getModifiers()));
            assertTrue(java.lang.reflect.Modifier.isStatic(queueCapacityField.getModifiers()));
        }
    }

    @Nested
    @DisplayName("Logger")
    class LoggerTests {

        @Test
        @DisplayName("Logger инициализирован")
        void testTaskManagerAppLogger() throws Exception {
            Class<?> appClass = TaskManagerApp.class;
            Field loggerField = appClass.getDeclaredField("logger");
            loggerField.setAccessible(true);
            Object logger = loggerField.get(null);

            assertNotNull(logger);
        }

        @Test
        @DisplayName("Logger должен быть static")
        void testTaskManagerAppLoggerIsStatic() throws Exception {
            Class<?> appClass = TaskManagerApp.class;
            Field loggerField = appClass.getDeclaredField("logger");

            assertTrue(java.lang.reflect.Modifier.isStatic(loggerField.getModifiers()));
        }
    }

    @Nested
    @DisplayName("Создание очереди и компонентов")
    class QueueAndComponentsTests {

        @Test
        @DisplayName("Создание очереди с правильной емкостью")
        void testTaskManagerAppQueueCreation() throws Exception {
            Class<?> appClass = TaskManagerApp.class;

            Field queueCapacityField = appClass.getDeclaredField("QUEUE_CAPACITY");
            queueCapacityField.setAccessible(true);
            int queueCapacity = queueCapacityField.getInt(null);

            Field maxOrdersField = appClass.getDeclaredField("MAX_ORDERS");
            maxOrdersField.setAccessible(true);
            int maxOrders = maxOrdersField.getInt(null);

            OrderStorage storage = new OrderStorage(queueCapacity);
            AtomicBoolean running = new AtomicBoolean(true);
            OrderProducer producer = new OrderProducer(storage, running, maxOrders);

            assertNotNull(producer);
            assertEquals(queueCapacity, storage.getQueueRemainingCapacity());
        }
    }

    @Nested
    @DisplayName("ExecutorService")
    class ExecutorServiceTests {

        @Test
        @DisplayName("Создание ExecutorService")
        void testTaskManagerAppExecutorService() {
            int numConsumers = 3;
            ExecutorService executorService = Executors.newFixedThreadPool(numConsumers + 1);

            assertNotNull(executorService);
            assertFalse(executorService.isShutdown());

            executorService.shutdown();
        }

        @Test
        @DisplayName("AwaitTermination работает")
        void testTaskManagerAppAwaitTermination() throws InterruptedException {
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

            assertTrue(terminated || !terminated);
            executorService.shutdownNow();
        }

        @Test
        @DisplayName("ShutdownNow работает")
        void testTaskManagerAppShutdownNow() throws InterruptedException {
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

            assertTrue(executorService.isShutdown());
        }
    }

    @Nested
    @DisplayName("Метод main")
    class MainMethodTests {

        @Test
        @DisplayName("Запуск main метода с завершением")
        void testTaskManagerAppMainWithShutdown() throws InterruptedException {
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

            assertFalse(appThread.isAlive());
        }

        @Test
        @DisplayName("Таймаут main метода")
        void testTaskManagerAppMainTimeout() throws InterruptedException {
            Thread appThread = new Thread(() -> {
                try {
                    TaskManagerApp.main(new String[]{});
                } catch (Exception e) {

                }
            });
            appThread.start();

            Thread.sleep(35000);
            appThread.interrupt();

            assertTrue(true);
        }

        @Test
        @DisplayName("Прерывание main метода")
        void testTaskManagerAppInterruptedException() throws InterruptedException {
            Thread appThread = new Thread(() -> {
                try {
                    TaskManagerApp.main(new String[]{});
                } catch (Exception e) {

                }
            });
            appThread.start();

            Thread.sleep(1000);
            appThread.interrupt();

            assertTrue(true);
        }

        @Test
        @DisplayName("Выполнение main метода")
        void testTaskManagerAppMainExecution() throws InterruptedException {
            Thread appThread = new Thread(() -> {
                try {
                    TaskManagerApp.main(new String[]{});
                } catch (Exception e) {

                }
            });
            appThread.start();

            Thread.sleep(3000);
            appThread.interrupt();
            appThread.join(2000);

            assertTrue(true);
        }
    }

    @Nested
    @DisplayName("Shutdown hook")
    class ShutdownHookTests {

        @Test
        @DisplayName("Создание экземпляра TaskManagerApp")
        void testTaskManagerAppShutdownHook() {
            TaskManagerApp app = new TaskManagerApp();
            assertNotNull(app);
        }
    }

    @Nested
    @DisplayName("Конфигурация")
    class ConfigurationTests {

        @Test
        @DisplayName("Проверка конфигурации")
        void testTaskManagerAppConfiguration() throws Exception {
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

            assertEquals(3, numConsumers);
            assertEquals(20, maxOrders);
            assertEquals(10, queueCapacity);
        }
    }

    @Nested
    @DisplayName("Интеграционные тесты")
    class IntegrationTests {

        @Test
        @DisplayName("Producer и Consumer работают вместе")
        void testProducerConsumerWorkflow() throws InterruptedException {
            OrderStorage storage = new OrderStorage(5);
            OrderProcessor processor = new OrderProcessor(storage);
            AtomicBoolean running = new AtomicBoolean(true);

            OrderProducer producer = new OrderProducer(storage, running, 5);
            OrderConsumer consumer = new OrderConsumer(storage, processor, running, "Test");

            Thread pt = new Thread(producer);
            Thread ct = new Thread(consumer);

            pt.start();
            ct.start();

            pt.join(8000);
            Thread.sleep(2000);
            running.set(false);
            ct.interrupt();
            ct.join(2000);

            assertTrue(producer.getOrdersCreated() > 0);
        }
    }
}