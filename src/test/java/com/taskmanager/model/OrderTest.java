package com.taskmanager.model;

import com.taskmanager.annotations.OrderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(order.getId());
        assertEquals(Order.Status.CREATED, order.getStatus());
        assertNotNull(order.getCreatedAt());
        assertEquals(OrderType.Priority.NORMAL, order.getPriority());
    }

    @Test
    void testParameterizedConstructor() {
        Order customOrder = new Order("John Doe", "Laptop", OrderType.Priority.URGENT);
        assertEquals("John Doe", customOrder.getCustomerName());
        assertEquals("Laptop", customOrder.getProduct());
        assertEquals(OrderType.Priority.URGENT, customOrder.getPriority());
    }

    @Test
    void testIdGetterSetter() {
        order.setId("test-id-123");
        assertEquals("test-id-123", order.getId());

        order.setId(null);
        assertNull(order.getId());
    }

    @Test
    void testCustomerNameGetterSetter() {
        order.setCustomerName("John Doe");
        assertEquals("John Doe", order.getCustomerName());

        order.setCustomerName(null);
        assertNull(order.getCustomerName());
    }

    @Test
    void testProductGetterSetter() {
        order.setProduct("Laptop");
        assertEquals("Laptop", order.getProduct());

        order.setProduct(null);
        assertNull(order.getProduct());
    }

    @Test
    void testPriorityGetterSetter() {
        order.setPriority(OrderType.Priority.URGENT);
        assertEquals(OrderType.Priority.URGENT, order.getPriority());

        order.setPriority(null);
        assertNull(order.getPriority());
    }

    @Test
    void testStatusGetterSetter() {
        order.setStatus(Order.Status.PROCESSING);
        assertEquals(Order.Status.PROCESSING, order.getStatus());

        order.setStatus(Order.Status.COMPLETED);
        assertEquals(Order.Status.COMPLETED, order.getStatus());

        order.setStatus(Order.Status.FAILED);
        assertEquals(Order.Status.FAILED, order.getStatus());
    }

    @Test
    void testCreatedAtGetterSetter() {
        LocalDateTime time = LocalDateTime.now();
        order.setCreatedAt(time);
        assertEquals(time, order.getCreatedAt());

        order.setCreatedAt(null);
        assertNull(order.getCreatedAt());
    }

    @Test
    void testProcessedAtGetterSetter() {
        LocalDateTime time = LocalDateTime.now();
        order.setProcessedAt(time);
        assertEquals(time, order.getProcessedAt());

        order.setProcessedAt(null);
        assertNull(order.getProcessedAt());
    }

    @Test
    void testProcessedByGetterSetter() {
        order.setProcessedBy("Processor-1");
        assertEquals("Processor-1", order.getProcessedBy());

        order.setProcessedBy(null);
        assertNull(order.getProcessedBy());
    }

    @Test
    void testIsUrgent() {
        order.setPriority(OrderType.Priority.URGENT);
        assertTrue(order.isUrgent());

        order.setPriority(OrderType.Priority.NORMAL);
        assertFalse(order.isUrgent());

        order.setPriority(null);
        assertFalse(order.isUrgent());
    }

    @Test
    void testEquals() {
        Order order1 = new Order("John", "Product", OrderType.Priority.NORMAL);
        Order order2 = new Order("Jane", "Other", OrderType.Priority.URGENT);

        assertTrue(order1.equals(order1));

        assertFalse(order1.equals(null));
        assertFalse(order1.equals("string"));

        order2.setId(order1.getId());
        assertTrue(order1.equals(order2));

        order2.setId("different-id");
        assertFalse(order1.equals(order2));

        order1.setId(null);
        order2.setId(null);
        assertTrue(order1.equals(order2));
    }

    @Test
    void testHashCode() {
        Order order1 = new Order("John", "Product", OrderType.Priority.NORMAL);
        Order order2 = new Order("Jane", "Other", OrderType.Priority.URGENT);

        int hashCode1 = order1.hashCode();
        int hashCode2 = order2.hashCode();

        assertNotEquals(hashCode1, hashCode2);

        order2.setId(order1.getId());
        assertEquals(order1.hashCode(), order2.hashCode());

        order1.setId(null);
        assertNotNull(order1.hashCode());
    }

    @Test
    void testToString() {
        Order testOrder = new Order("Alice", "MacBook", OrderType.Priority.URGENT);
        testOrder.setStatus(Order.Status.COMPLETED);

        String result = testOrder.toString();

        String shortId = testOrder.getId().substring(0, 8);
        assertTrue(result.contains(shortId));
        assertFalse(result.contains(testOrder.getId()));

        assertTrue(result.contains("Alice"));
        assertTrue(result.contains("MacBook"));
        assertTrue(result.contains("URGENT"));
        assertTrue(result.contains("COMPLETED"));

        assertTrue(result.startsWith("Order{"));
        assertTrue(result.endsWith("}"));

        Order nullOrder = new Order();
        nullOrder.setCustomerName(null);
        nullOrder.setProduct(null);
        nullOrder.setPriority(null);
        assertNotNull(nullOrder.toString());
    }

    @Test
    void testEnums() {
        Order.Status[] statuses = Order.Status.values();
        assertEquals(4, statuses.length);
        assertEquals("CREATED", Order.Status.CREATED.name());
        assertEquals("PROCESSING", Order.Status.PROCESSING.name());
        assertEquals("COMPLETED", Order.Status.COMPLETED.name());
        assertEquals("FAILED", Order.Status.FAILED.name());

        assertEquals(2, OrderType.Priority.values().length);
        assertEquals("URGENT", OrderType.Priority.URGENT.name());
        assertEquals("NORMAL", OrderType.Priority.NORMAL.name());
    }
}