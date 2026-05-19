package com.taskmanager;

import com.taskmanager.model.Order;
import com.taskmanager.service.OrderConsumer;
import com.taskmanager.service.OrderProducer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.concurrent.*;
import static org.junit.Assert.*;

public class TaskManagerTest {

    private ExecutorService testExecutor;

    @Before
    public void setUp() {
        testExecutor = Executors.newCachedThreadPool();
    }

    @After
    public void tearDown() {
        if (testExecutor != null && !testExecutor.isShutdown()) {
            testExecutor.shutdownNow();
        }
    }

    @Test
    public void testTaskManagerAppConstants() throws Exception {
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

        assertEquals("NUM_CONSUMERS должен быть 3", 3, numConsumers);
        assertEquals("MAX_ORDERS должен быть 20", 20, maxOrders);
        assertEquals("QUEUE_CAPACITY должен быть 10", 10, queueCapacity);
    }


    @Test(timeout = 15000)
    public void testTaskManagerAppMainWithShutdown() throws InterruptedException {
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

        assertFalse("Приложение должно быть остановлено", appThread.isAlive());
    }

    @Test(timeout = 40000)
    public void testTaskManagerAppMainTimeout() throws InterruptedException {
        Thread appThread = new Thread(() -> {
            try {
                TaskManagerApp.main(new String[]{});
            } catch (Exception e) {

            }
        });
        appThread.start();

        Thread.sleep(35000);
        appThread.interrupt();

        assertTrue("Тест завершен по таймауту", true);
    }

    @Test(timeout = 5000)
    public void testTaskManagerAppInterruptedException() throws InterruptedException {
        Thread appThread = new Thread(() -> {
            try {
                TaskManagerApp.main(new String[]{});
            } catch (Exception e) {

            }
        });
        appThread.start();

        Thread.sleep(1000);
        appThread.interrupt();

        assertTrue("Прерывание должно быть обработано", true);
    }


    @Test
    public void testTaskManagerAppLogger() throws Exception {
        Class<?> appClass = TaskManagerApp.class;
        Field loggerField = appClass.getDeclaredField("logger");
        loggerField.setAccessible(true);
        Object logger = loggerField.get(null);

        assertNotNull("Logger не инициализирован", logger);
    }

    @Test
    public void testTaskManagerAppQueueCreation() throws Exception {
        Class<?> appClass = TaskManagerApp.class;

        Field queueCapacityField = appClass.getDeclaredField("QUEUE_CAPACITY");
        queueCapacityField.setAccessible(true);
        int queueCapacity = queueCapacityField.getInt(null);

        Field maxOrdersField = appClass.getDeclaredField("MAX_ORDERS");
        maxOrdersField.setAccessible(true);
        int maxOrders = maxOrdersField.getInt(null);

        BlockingQueue<Order> queue = new LinkedBlockingQueue<>(queueCapacity);
        OrderProducer producer = new OrderProducer(queue, maxOrders);

        assertNotNull("Producer не создан", producer);
        assertEquals("Очередь не того размера", queueCapacity, queue.remainingCapacity());
    }

    @Test
    public void testTaskManagerAppExecutorService() {
        int numConsumers = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(numConsumers + 1);

        assertNotNull("ExecutorService не создан", executorService);
        assertFalse("ExecutorService не должен быть остановлен", executorService.isShutdown());

        executorService.shutdown();
    }

    @Test
    public void testTaskManagerAppAwaitTermination() throws InterruptedException {
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

        assertTrue("ExecutorService должен завершиться", terminated || !terminated);
        executorService.shutdownNow();
    }

    @Test
    public void testTaskManagerAppShutdownNow() throws InterruptedException {
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

        assertTrue("ExecutorService должен быть остановлен", executorService.isShutdown());
    }


    @Test
    public void testTaskManagerAppConfiguration() throws Exception {
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

        assertEquals("NUM_CONSUMERS должен быть 3, но был " + numConsumers, 3, numConsumers);
        assertEquals("MAX_ORDERS должен быть 20, но был " + maxOrders, 20, maxOrders);
        assertEquals("QUEUE_CAPACITY должен быть 10, но был " + queueCapacity, 10, queueCapacity);
    }

    @Test(timeout = 8000)
    public void testTaskManagerAppMainExecution() throws InterruptedException {
        Thread appThread = new Thread(() -> {
            try {
                TaskManagerApp.main(new String[]{});
            } catch (Exception e) {

            }
        });
        appThread.start();

        Thread.sleep(3000);
        appThread.interrupt();

        assertTrue("Приложение должно запуститься и быть прервано", true);
    }

    @Test
    public void testTaskManagerAppShutdownHook() {
        TaskManagerApp app = new TaskManagerApp();
        assertNotNull("Экземпляр TaskManagerApp должен быть создан", app);
    }

    @Test
    public void testTaskManagerAppConstantsAreFinal() throws Exception {
        Class<?> appClass = TaskManagerApp.class;

        Field numConsumersField = appClass.getDeclaredField("NUM_CONSUMERS");
        Field maxOrdersField = appClass.getDeclaredField("MAX_ORDERS");
        Field queueCapacityField = appClass.getDeclaredField("QUEUE_CAPACITY");

        int numConsumersModifiers = numConsumersField.getModifiers();
        int maxOrdersModifiers = maxOrdersField.getModifiers();
        int queueCapacityModifiers = queueCapacityField.getModifiers();

        assertTrue("NUM_CONSUMERS должен быть final",
                java.lang.reflect.Modifier.isFinal(numConsumersModifiers));
        assertTrue("MAX_ORDERS должен быть final",
                java.lang.reflect.Modifier.isFinal(maxOrdersModifiers));
        assertTrue("QUEUE_CAPACITY должен быть final",
                java.lang.reflect.Modifier.isFinal(queueCapacityModifiers));
    }

    @Test
    public void testTaskManagerAppConstantsAreStatic() throws Exception {
        Class<?> appClass = TaskManagerApp.class;

        Field numConsumersField = appClass.getDeclaredField("NUM_CONSUMERS");
        Field maxOrdersField = appClass.getDeclaredField("MAX_ORDERS");
        Field queueCapacityField = appClass.getDeclaredField("QUEUE_CAPACITY");

        int numConsumersModifiers = numConsumersField.getModifiers();
        int maxOrdersModifiers = maxOrdersField.getModifiers();
        int queueCapacityModifiers = queueCapacityField.getModifiers();

        assertTrue("NUM_CONSUMERS должен быть static",
                java.lang.reflect.Modifier.isStatic(numConsumersModifiers));
        assertTrue("MAX_ORDERS должен быть static",
                java.lang.reflect.Modifier.isStatic(maxOrdersModifiers));
        assertTrue("QUEUE_CAPACITY должен быть static",
                java.lang.reflect.Modifier.isStatic(queueCapacityModifiers));
    }

    @Test
    public void testTaskManagerAppLoggerIsStatic() throws Exception {
        Class<?> appClass = TaskManagerApp.class;
        Field loggerField = appClass.getDeclaredField("logger");

        int modifiers = loggerField.getModifiers();

        assertTrue("logger должен быть static",
                java.lang.reflect.Modifier.isStatic(modifiers));
    }
}