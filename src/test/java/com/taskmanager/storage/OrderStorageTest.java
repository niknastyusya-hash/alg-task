package com.taskmanager.storage;

import com.taskmanager.annotations.OrderType;
import com.taskmanager.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderStorage тесты")
class OrderStorageTest {

    private OrderStorage storage;

    @BeforeEach
    void setUp() {
        storage = new OrderStorage(5);
    }

    @Nested
    @DisplayName("Создание и базовая работа")
    class CreationTests {

        @Test
        @DisplayName("Создание хранилища")
        void testStorageCreation() {
            assertNotNull(storage);
            assertEquals(0, storage.getQueueSize());
            assertEquals(0, storage.getProcessedOrdersCount());
        }
    }

    @Nested
    @DisplayName("Работа с очередью")
    class QueueTests {

        @Test
        @DisplayName("Добавление заказа")
        void testAddOrder() {
            Order order = new Order("John", "Product", OrderType.Priority.NORMAL);
            assertTrue(storage.addOrder(order));
            assertEquals(1, storage.getQueueSize());
        }

        @Test
        @DisplayName("Добавление при полной очереди")
        void testAddOrderWhenFull() {
            for (int i = 0; i < 5; i++) {
                storage.addOrder(new Order("C" + i, "P" + i, OrderType.Priority.NORMAL));
            }
            Order extra = new Order("Extra", "Extra", OrderType.Priority.NORMAL);
            assertFalse(storage.addOrder(extra));
            assertEquals(5, storage.getQueueSize());
        }

        @Test
        @DisplayName("Poll заказа")
        void testPollOrder() throws InterruptedException {
            Order order = new Order("John", "Product", OrderType.Priority.NORMAL);
            storage.addOrder(order);

            Order polled = storage.pollOrder(1, TimeUnit.SECONDS);
            assertNotNull(polled);
            assertEquals(order.getId(), polled.getId());
            assertEquals(0, storage.getQueueSize());
        }

        @Test
        @DisplayName("Poll из пустой очереди")
        void testPollOrderFromEmptyQueue() throws InterruptedException {
            Order polled = storage.pollOrder(1, TimeUnit.SECONDS);
            assertNull(polled);
        }

        @Test
        @DisplayName("Take заказа")
        void testTakeOrder() throws InterruptedException {
            Order order = new Order("John", "Product", OrderType.Priority.NORMAL);
            storage.addOrder(order);

            Order taken = storage.takeOrder();
            assertNotNull(taken);
            assertEquals(order.getId(), taken.getId());
        }
    }

    @Nested
    @DisplayName("Работа с обработанными заказами")
    class ProcessedOrdersTests {

        @Test
        @DisplayName("Сохранение обработанного заказа")
        void testStoreProcessedOrder() {
            Order order = new Order("John", "Product", OrderType.Priority.NORMAL);
            storage.storeProcessedOrder(order);

            assertEquals(1, storage.getProcessedOrdersCount());
            assertTrue(storage.isOrderProcessed(order.getId()));
            assertNotNull(storage.getProcessedOrder(order.getId()));
        }

        @Test
        @DisplayName("Получение всех обработанных заказов")
        void testGetAllProcessedOrders() {
            Order order1 = new Order("John", "Product1", OrderType.Priority.NORMAL);
            Order order2 = new Order("Jane", "Product2", OrderType.Priority.URGENT);

            storage.storeProcessedOrder(order1);
            storage.storeProcessedOrder(order2);

            var allOrders = storage.getAllProcessedOrders();
            assertEquals(2, allOrders.size());
            assertTrue(allOrders.containsKey(order1.getId()));
            assertTrue(allOrders.containsKey(order2.getId()));
        }

        @Test
        @DisplayName("Очистка хранилища")
        void testClear() {
            storage.addOrder(new Order("John", "Product", OrderType.Priority.NORMAL));
            storage.storeProcessedOrder(new Order("Jane", "Product", OrderType.Priority.URGENT));

            storage.clear();

            assertEquals(0, storage.getQueueSize());
            assertEquals(0, storage.getProcessedOrdersCount());
        }
    }

    @Nested
    @DisplayName("Тесты конструктора ")
    class ConstructorCoverageTests {

        @Test
        @DisplayName("Конструктор с большой вместимостью")
        void testConstructorWithLargeCapacity() {
            OrderStorage largeStorage = new OrderStorage(10000);
            assertEquals(10000, largeStorage.getQueueRemainingCapacity());
            assertEquals(0, largeStorage.getQueueSize());
        }
    }

    @Nested
    @DisplayName("тесты addOrder")
    class AdditionalAddOrderTests {


        @Test
        @DisplayName("addOrder после clear")
        void testAddOrderAfterClear() {
            for (int i = 0; i < 3; i++) {
                storage.addOrder(new Order("C" + i, "P" + i, null));
            }
            storage.clear();
            assertEquals(0, storage.getQueueSize());

            Order newOrder = new Order("New", "New", null);
            assertTrue(storage.addOrder(newOrder));
            assertEquals(1, storage.getQueueSize());
        }

        @Test
        @DisplayName("addOrder с одним и тем же заказом несколько раз")
        void testAddOrderSameOrderMultipleTimes() {
            Order order = new Order("John", "Product", null);
            assertTrue(storage.addOrder(order));
            assertTrue(storage.addOrder(order));
            assertEquals(2, storage.getQueueSize());
        }
    }

    @Nested
    @DisplayName("тесты pollOrder")
    class AdditionalPollOrderTests {

        @Test
        @DisplayName("pollOrder с нулевым таймаутом")
        void testPollOrderWithZeroTimeout() throws InterruptedException {
            Order order = new Order("John", "Product", null);
            storage.addOrder(order);

            Order polled = storage.pollOrder(0, TimeUnit.SECONDS);
            assertNotNull(polled);
        }

        @Test
        @DisplayName("pollOrder с отрицательным таймаутом")
        void testPollOrderWithNegativeTimeout() throws InterruptedException {
            Order order = new Order("John", "Product", null);
            storage.addOrder(order);

            Order polled = storage.pollOrder(-1, TimeUnit.SECONDS);
            assertNotNull(polled);
        }

        @Test
        @DisplayName("pollOrder с разными единицами времени")
        void testPollOrderWithDifferentTimeUnits() throws InterruptedException {
            Order order = new Order("John", "Product", null);
            storage.addOrder(order);

            Order polledMillis = storage.pollOrder(100, TimeUnit.MILLISECONDS);
            assertNotNull(polledMillis);

            storage.addOrder(order);
            Order polledNanos = storage.pollOrder(1000000, TimeUnit.NANOSECONDS);
            assertNotNull(polledNanos);
        }

        @Test
        @DisplayName("pollOrder после очистки очереди")
        void testPollOrderAfterClear() throws InterruptedException {
            storage.addOrder(new Order("John", "Product", null));
            storage.clear();

            Order polled = storage.pollOrder(1, TimeUnit.SECONDS);
            assertNull(polled);
        }
    }

    @Nested
    @DisplayName("тесты takeOrder")
    class AdditionalTakeOrderTests {

        @Test
        @DisplayName("takeOrder с несколькими заказами")
        void testTakeOrderWithMultipleOrders() throws InterruptedException {
            Order order1 = new Order("John", "Product1", null);
            Order order2 = new Order("Jane", "Product2", null);
            Order order3 = new Order("Bob", "Product3", null);

            storage.addOrder(order1);
            storage.addOrder(order2);
            storage.addOrder(order3);

            Order taken1 = storage.takeOrder();
            Order taken2 = storage.takeOrder();
            Order taken3 = storage.takeOrder();

            assertNotNull(taken1);
            assertNotNull(taken2);
            assertNotNull(taken3);
            assertEquals(0, storage.getQueueSize());
        }
    }

    @Nested
    @DisplayName("Тесты getQueueSize и getQueueRemainingCapacity")
    class AdditionalQueueMetricsTests {

        @Test
        @DisplayName("getQueueSize после poll")
        void testGetQueueSizeAfterPoll() throws InterruptedException {
            storage.addOrder(new Order("John", "Product", null));
            storage.addOrder(new Order("Jane", "Product", null));
            assertEquals(2, storage.getQueueSize());

            storage.pollOrder(1, TimeUnit.SECONDS);
            assertEquals(1, storage.getQueueSize());
        }

        @Test
        @DisplayName("getQueueRemainingCapacity после take")
        void testGetQueueRemainingCapacityAfterTake() throws InterruptedException {
            for (int i = 0; i < 3; i++) {
                storage.addOrder(new Order("C" + i, "P" + i, null));
            }
            assertEquals(2, storage.getQueueRemainingCapacity());

            storage.takeOrder();
            assertEquals(3, storage.getQueueRemainingCapacity());
        }

        @Test
        @DisplayName("getQueueRemainingCapacity при полной очереди")
        void testGetQueueRemainingCapacityWhenFull() {
            for (int i = 0; i < 5; i++) {
                storage.addOrder(new Order("C" + i, "P" + i, null));
            }
            assertEquals(0, storage.getQueueRemainingCapacity());
        }
    }

    @Nested
    @DisplayName("тесты storeProcessedOrder")
    class AdditionalStoreProcessedOrderTests {

        @Test
        @DisplayName("storeProcessedOrder с заказом имеющим null ID")
        void testStoreProcessedOrderWithNullId() {
            Order order = new Order("John", "Product", null);
            order.setId(null);

            assertThrows(NullPointerException.class, () -> {
                storage.storeProcessedOrder(order);
            });
        }

        @Test
        @DisplayName("storeProcessedOrder с одинаковыми ID")
        void testStoreProcessedOrderWithSameId() {
            Order order1 = new Order("John", "Product1", null);
            Order order2 = new Order("Jane", "Product2", null);
            order2.setId(order1.getId());

            storage.storeProcessedOrder(order1);
            storage.storeProcessedOrder(order2);

            assertEquals(1, storage.getProcessedOrdersCount());
            Order retrieved = storage.getProcessedOrder(order1.getId());
            assertEquals("Product2", retrieved.getProduct());
        }

        @Test
        @DisplayName("storeProcessedOrder со 100 заказами")
        void testStoreProcessedOrderWithManyOrders() {
            for (int i = 0; i < 100; i++) {
                Order order = new Order("Customer" + i, "Product" + i, null);
                storage.storeProcessedOrder(order);
            }
            assertEquals(100, storage.getProcessedOrdersCount());
        }
    }

    @Nested
    @DisplayName("тесты getProcessedOrder")
    class AdditionalGetProcessedOrderTests {

        @Test
        @DisplayName("getProcessedOrder с пустой строкой")
        void testGetProcessedOrderWithEmptyString() {
            Order result = storage.getProcessedOrder("");
            assertNull(result);
        }

        @Test
        @DisplayName("getProcessedOrder с несуществующим ID")
        void testGetProcessedOrderWithNonExistentId() {
            Order result = storage.getProcessedOrder("non-existent-id-12345");
            assertNull(result);
        }

        @Test
        @DisplayName("getProcessedOrder после удаления заказа")
        void testGetProcessedOrderAfterClear() {
            Order order = new Order("John", "Product", null);
            storage.storeProcessedOrder(order);
            assertNotNull(storage.getProcessedOrder(order.getId()));

            storage.clear();
            assertNull(storage.getProcessedOrder(order.getId()));
        }
    }

    @Nested
    @DisplayName("тесты getAllProcessedOrders")
    class AdditionalGetAllProcessedOrdersTests {

        @Test
        @DisplayName("getAllProcessedOrders с пустым хранилищем")
        void testGetAllProcessedOrdersFromEmpty() {
            var allOrders = storage.getAllProcessedOrders();
            assertTrue(allOrders.isEmpty());
            assertEquals(0, allOrders.size());
        }

        @Test
        @DisplayName("getAllProcessedOrders возвращает копию, а не ссылку")
        void testGetAllProcessedOrdersReturnsCopy() {
            Order order = new Order("John", "Product", null);
            storage.storeProcessedOrder(order);

            var allOrders = storage.getAllProcessedOrders();
            allOrders.clear();

            assertEquals(1, storage.getProcessedOrdersCount());
        }

        @Test
        @DisplayName("getAllProcessedOrders после нескольких сохранений")
        void testGetAllProcessedOrdersAfterMultipleStores() {
            for (int i = 0; i < 10; i++) {
                storage.storeProcessedOrder(new Order("C" + i, "P" + i, null));
            }

            var allOrders = storage.getAllProcessedOrders();
            assertEquals(10, allOrders.size());
        }
    }

    @Nested
    @DisplayName("тесты getProcessedOrdersCount")
    class AdditionalGetProcessedOrdersCountTests {

        @Test
        @DisplayName("getProcessedOrdersCount после перезаписи")
        void testGetProcessedOrdersCountAfterOverwrite() {
            Order order = new Order("John", "Product1", null);
            storage.storeProcessedOrder(order);
            assertEquals(1, storage.getProcessedOrdersCount());

            Order sameIdOrder = new Order("Jane", "Product2", null);
            sameIdOrder.setId(order.getId());
            storage.storeProcessedOrder(sameIdOrder);

            assertEquals(1, storage.getProcessedOrdersCount());
        }

        @Test
        @DisplayName("getProcessedOrdersCount после clear")
        void testGetProcessedOrdersCountAfterClear() {
            storage.storeProcessedOrder(new Order("John", "Product", null));
            storage.storeProcessedOrder(new Order("Jane", "Product", null));
            assertEquals(2, storage.getProcessedOrdersCount());

            storage.clear();
            assertEquals(0, storage.getProcessedOrdersCount());
        }
    }

    @Nested
    @DisplayName("тесты isOrderProcessed")
    class AdditionalIsOrderProcessedTests {


        @Test
        @DisplayName("isOrderProcessed с пустой строкой")
        void testIsOrderProcessedWithEmptyString() {
            assertFalse(storage.isOrderProcessed(""));
        }

        @Test
        @DisplayName("isOrderProcessed с несуществующим ID")
        void testIsOrderProcessedWithNonExistentId() {
            assertFalse(storage.isOrderProcessed("fake-id"));
        }

        @Test
        @DisplayName("isOrderProcessed после удаления заказа")
        void testIsOrderProcessedAfterClear() {
            Order order = new Order("John", "Product", null);
            storage.storeProcessedOrder(order);
            assertTrue(storage.isOrderProcessed(order.getId()));

            storage.clear();
            assertFalse(storage.isOrderProcessed(order.getId()));
        }
    }

    @Nested
    @DisplayName("тесты clear")
    class AdditionalClearTests {

        @Test
        @DisplayName("clear на пустом хранилище")
        void testClearOnEmptyStorage() {
            storage.clear();
            assertEquals(0, storage.getQueueSize());
            assertEquals(0, storage.getProcessedOrdersCount());
            assertEquals(5, storage.getQueueRemainingCapacity());
        }

        @Test
        @DisplayName("clear после частичного заполнения")
        void testClearAfterPartialFill() {
            storage.addOrder(new Order("John", "Product", null));
            storage.storeProcessedOrder(new Order("Jane", "Product", null));

            storage.clear();

            assertEquals(0, storage.getQueueSize());
            assertEquals(0, storage.getProcessedOrdersCount());
        }

        @Test
        @DisplayName("clear после многократного использования")
        void testClearAfterRepeatedUse() {
            for (int i = 0; i < 5; i++) {
                storage.addOrder(new Order("C" + i, "P" + i, null));
            }
            for (int i = 0; i < 3; i++) {
                storage.storeProcessedOrder(new Order("PC" + i, "PP" + i, null));
            }

            storage.clear();

            assertEquals(0, storage.getQueueSize());
            assertEquals(0, storage.getProcessedOrdersCount());
            assertEquals(5, storage.getQueueRemainingCapacity());

            assertTrue(storage.addOrder(new Order("New", "New", null)));
            assertEquals(1, storage.getQueueSize());
        }
    }

    @Nested
    @DisplayName("граничные случаи")
    class StressAndBoundaryTests {

        @Test
        @DisplayName("Многопоточное добавление в очередь")
        void testConcurrentAddOrder() throws InterruptedException {
            int threadCount = 10;
            int ordersPerThread = 10;
            Thread[] threads = new Thread[threadCount];

            for (int i = 0; i < threadCount; i++) {
                final int threadId = i;
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < ordersPerThread; j++) {
                        storage.addOrder(new Order("C" + threadId + "_" + j, "P" + threadId + "_" + j, null));
                    }
                });
                threads[i].start();
            }

            for (Thread t : threads) {
                t.join();
            }
            assertTrue(storage.getQueueSize() <= 5);
        }

        @Test
        @DisplayName("Многопоточное сохранение обработанных заказов")
        void testConcurrentStoreProcessedOrder() throws InterruptedException {
            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];

            for (int i = 0; i < threadCount; i++) {
                final int threadId = i;
                threads[i] = new Thread(() -> {
                    Order order = new Order("C" + threadId, "P" + threadId, null);
                    storage.storeProcessedOrder(order);
                });
                threads[i].start();
            }

            for (Thread t : threads) {
                t.join();
            }

            assertEquals(threadCount, storage.getProcessedOrdersCount());
        }

    }
}
