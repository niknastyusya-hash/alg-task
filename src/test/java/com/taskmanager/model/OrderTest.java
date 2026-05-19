package com.taskmanager.model;

import com.taskmanager.annotations.OrderType;
import org.junit.Test;
import java.time.LocalDateTime;
import static org.junit.Assert.*;

public class OrderTest {

    @Test
    public void testOrderCreation() {
        Order order = new Order("Тестовый заказ", "REGULAR");

        assertNotNull("id не должен быть null", order.getId());
        assertNotNull("createdAt не должен быть null", order.getCreatedAt());
        assertEquals("description не совпадает", "Тестовый заказ", order.getDescription());
        assertEquals("type не совпадает", "REGULAR", order.getType());
        assertFalse("processed должен быть false", order.isProcessed());
        assertFalse("urgent должен быть false", order.isUrgent());
    }

    @Test
    public void testOrderCreationWithUrgentType() {
        Order order = new Order("Срочный заказ", "URGENT");

        assertEquals("description не совпадает", "Срочный заказ", order.getDescription());
        assertEquals("type должен быть URGENT", "URGENT", order.getType());
        assertTrue("urgent должен быть true", order.isUrgent());
        assertFalse("processed должен быть false", order.isProcessed());
    }

    @Test
    public void testOrderDefaultConstructor() {
        Order order = new Order();

        assertNotNull("Конструктор без параметров не создал id", order.getId());
        assertNotNull("Конструктор без параметров не создал createdAt", order.getCreatedAt());
        assertNull("description должен быть null", order.getDescription());
        assertNull("type должен быть null", order.getType());
        assertFalse("processed должен быть false", order.isProcessed());
        assertFalse("urgent должен быть false", order.isUrgent());
    }

    @Test
    public void testOrderIsUrgentReturnsTrueForUrgentType() {
        Order order = new Order("Срочный заказ", "URGENT");
        assertTrue("URGENT тип должен возвращать true", order.isUrgent());
    }

    @Test
    public void testOrderIsUrgentReturnsFalseForRegularType() {
        Order order = new Order("Обычный заказ", "REGULAR");
        assertFalse("REGULAR тип должен возвращать false", order.isUrgent());
    }

    @Test
    public void testOrderIsUrgentReturnsFalseForNullType() {
        Order order = new Order("Тест", null);
        assertFalse("null тип должен возвращать false", order.isUrgent());
    }

    @Test
    public void testOrderIsUrgentReturnsFalseForEmptyType() {
        Order order = new Order("Тест", "");
        assertFalse("пустой тип должен возвращать false", order.isUrgent());
    }

    @Test
    public void testOrderIsUrgentCaseInsensitive() {
        Order orderLower = new Order("Тест", "urgent");
        Order orderMixed = new Order("Тест", "UrGeNt");

        assertTrue("lowercase urgent должен возвращать true", orderLower.isUrgent());
        assertTrue("mixed case urgent должен возвращать true", orderMixed.isUrgent());
    }

    @Test
    public void testOrderSettersAndGetters() {
        Order order = new Order();
        LocalDateTime now = LocalDateTime.now();

        order.setId("test-id-123");
        order.setDescription("Тестовое описание");
        order.setType("URGENT");
        order.setProcessed(true);
        order.setProcessedBy("Тестер");
        order.setProcessedAt(now);
        order.setCreatedAt(now);

        assertEquals("id не установился", "test-id-123", order.getId());
        assertEquals("description не установился", "Тестовое описание", order.getDescription());
        assertEquals("type не установился", "URGENT", order.getType());
        assertTrue("processed не установился", order.isProcessed());
        assertEquals("processedBy не установился", "Тестер", order.getProcessedBy());
        assertEquals("processedAt не установился", now, order.getProcessedAt());
        assertEquals("createdAt не установился", now, order.getCreatedAt());
    }

    @Test
    public void testOrderSettersWithNullValues() {
        Order order = new Order("Начальное описание", "REGULAR");

        order.setDescription(null);
        order.setProcessedBy(null);
        order.setProcessedAt(null);
        order.setCreatedAt(null);

        assertNull("description может быть null", order.getDescription());
        assertNull("processedBy может быть null", order.getProcessedBy());
        assertNull("processedAt может быть null", order.getProcessedAt());
        assertNull("createdAt может быть null", order.getCreatedAt());
    }

    @Test
    public void testOrderToString() {
        Order order = new Order("Тест toString", "REGULAR");
        String toString = order.toString();

        assertNotNull("toString вернул null", toString);
        assertTrue("toString не содержит описание", toString.contains("Тест toString"));
        assertTrue("toString не содержит тип", toString.contains("REGULAR"));
        assertTrue("toString не содержит id", toString.contains(order.getId()));
        assertTrue("toString не содержит processed", toString.contains(String.valueOf(order.isProcessed())));
    }

    @Test
    public void testOrderEqualsAndHashCode() {
        Order order1 = new Order("Тест", "REGULAR");
        Order order2 = new Order("Тест", "REGULAR");
        Order order3 = new Order("Другой", "URGENT");

        assertNotEquals("Разные заказы не должны быть равны", order1, order2);
        assertEquals("Заказ должен быть равен сам себе", order1, order1);
        assertNotEquals("Разные заказы с разными id не равны", order1, order3);
        assertNotEquals("HashCode разных заказов не должны быть равны", order1.hashCode(), order2.hashCode());
    }

    @Test
    public void testOrderEqualsSameId() {
        Order order1 = new Order("Тест", "REGULAR");
        Order order2 = new Order("Другой текст", "URGENT");

        order2.setId(order1.getId());

        assertEquals("Заказы с одинаковым id должны быть равны", order1, order2);
        assertEquals("HashCode должны быть одинаковыми", order1.hashCode(), order2.hashCode());
    }

    @Test
    public void testOrderEqualsWithNull() {
        Order order = new Order("Тест", "REGULAR");
        assertNotEquals("Сравнение с null должно быть false", order, null);
    }

    @Test
    public void testOrderEqualsWithDifferentClass() {
        Order order = new Order("Тест", "REGULAR");
        String notOrder = "не заказ";
        assertNotEquals("Сравнение с другим классом должно быть false", order, notOrder);
        assertNotEquals("Сравнение с Integer должно быть false", order, 123);
    }

    @Test
    public void testOrderHashCodeConsistency() {
        Order order = new Order("Тест", "REGULAR");
        int hashCode1 = order.hashCode();
        int hashCode2 = order.hashCode();
        int hashCode3 = order.hashCode();

        assertEquals("HashCode должен быть consistent", hashCode1, hashCode2);
        assertEquals("HashCode должен быть consistent", hashCode1, hashCode3);
    }

    @Test
    public void testOrderHashCodeBasedOnIdOnly() {
        Order order1 = new Order("Тест1", "REGULAR");
        Order order2 = new Order("Тест2", "URGENT");

        order2.setId(order1.getId());

        assertEquals("HashCode должен зависеть только от id", order1.hashCode(), order2.hashCode());
    }

    @Test
    public void testOrderProcessedStatus() {
        Order order = new Order("Тест", "REGULAR");

        assertFalse("Новый заказ не должен быть обработан", order.isProcessed());
        assertNull("processedAt должен быть null", order.getProcessedAt());
        assertNull("processedBy должен быть null", order.getProcessedBy());

        order.setProcessed(true);
        assertTrue("После setProcessed(true) должен быть обработан", order.isProcessed());

        order.setProcessed(false);
        assertFalse("После setProcessed(false) не должен быть обработан", order.isProcessed());
    }

    @Test
    public void testOrderWithEmptyDescription() {
        Order order = new Order("", "REGULAR");

        assertNotNull("Описание не должно быть null", order.getDescription());
        assertTrue("Описание должно быть пустым", order.getDescription().isEmpty());
        assertEquals("Описание должно быть пустой строкой", "", order.getDescription());
    }

    @Test
    public void testOrderWithWhitespaceDescription() {
        Order order = new Order("   ", "REGULAR");

        assertNotNull("Описание не должно быть null", order.getDescription());
        assertEquals("Описание должно содержать пробелы", "   ", order.getDescription());
    }

    @Test
    public void testOrderWithLongDescription() {
        String longDesc = "A".repeat(300);
        Order order = new Order(longDesc, "REGULAR");

        assertEquals("Длинное описание не сохранилось", longDesc, order.getDescription());
        assertEquals("Длина описания должна быть 300", 300, order.getDescription().length());
    }

    @Test
    public void testOrderWithVeryLongDescription() {
        String veryLongDesc = "B".repeat(1000);
        Order order = new Order(veryLongDesc, "URGENT");

        assertEquals("Очень длинное описание не сохранилось", veryLongDesc, order.getDescription());
        assertEquals("Длина описания должна быть 1000", 1000, order.getDescription().length());
    }

    @Test
    public void testOrderCreatedAtIsSetOnConstruction() throws InterruptedException {
        LocalDateTime before = LocalDateTime.now();
        Thread.sleep(1);
        Order order = new Order("Тест", "REGULAR");
        Thread.sleep(1);
        LocalDateTime after = LocalDateTime.now();

        assertNotNull("createdAt не должен быть null", order.getCreatedAt());
        assertTrue("createdAt должен быть после before", order.getCreatedAt().isAfter(before) || order.getCreatedAt().equals(before));
        assertTrue("createdAt должен быть до after", order.getCreatedAt().isBefore(after) || order.getCreatedAt().equals(after));
    }

    @Test
    public void testOrderIdIsUnique() {
        Order order1 = new Order("Заказ 1", "REGULAR");
        Order order2 = new Order("Заказ 2", "REGULAR");
        Order order3 = new Order("Заказ 3", "URGENT");

        assertNotEquals("ID заказов должны быть разными", order1.getId(), order2.getId());
        assertNotEquals("ID заказов должны быть разными", order1.getId(), order3.getId());
        assertNotEquals("ID заказов должны быть разными", order2.getId(), order3.getId());
    }

    @Test
    public void testOrderIdFormat() {
        Order order = new Order("Тест", "REGULAR");
        String id = order.getId();

        assertNotNull("ID не должен быть null", id);
        assertTrue("ID не должен быть пустым", !id.isEmpty());
        assertTrue("ID должен соответствовать формату UUID", id.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
    }

    @Test
    public void testOrderProcessedByAndProcessedAt() {
        Order order = new Order("Тест", "REGULAR");
        LocalDateTime now = LocalDateTime.now();

        order.setProcessed(true);
        order.setProcessedAt(now);
        order.setProcessedBy("Обработчик");

        assertTrue("Заказ должен быть обработан", order.isProcessed());
        assertEquals("processedAt должен быть установлен", now, order.getProcessedAt());
        assertEquals("processedBy должен быть установлен", "Обработчик", order.getProcessedBy());
    }

    @Test
    public void testOrderWithDifferentTypes() {
        Order urgent = new Order("Срочный", "URGENT");
        Order regular = new Order("Обычный", "REGULAR");
        Order unknown = new Order("Неизвестный", "UNKNOWN");

        assertTrue("URGENT должен быть срочным", urgent.isUrgent());
        assertFalse("REGULAR не должен быть срочным", regular.isUrgent());
        assertFalse("UNKNOWN не должен быть срочным", unknown.isUrgent());
    }

    @Test
    public void testOrderTypeEnumValues() {
        assertEquals("URGENT", OrderType.Type.URGENT.name());
        assertEquals("REGULAR", OrderType.Type.REGULAR.name());
        assertEquals(2, OrderType.Type.values().length);
    }

    @Test
    public void testOrderMultipleInstancesHaveDifferentTimestamps() throws InterruptedException {
        Order order1 = new Order("Первый", "REGULAR");
        Thread.sleep(10);
        Order order2 = new Order("Второй", "URGENT");

        assertNotNull(order1.getCreatedAt());
        assertNotNull(order2.getCreatedAt());
        assertTrue("Временные метки должны отличаться",
                order2.getCreatedAt().isAfter(order1.getCreatedAt()) ||
                        order2.getCreatedAt().equals(order1.getCreatedAt()));
    }

    @Test
    public void testOrderSetProcessedToTrueThenFalse() {
        Order order = new Order("Тест", "REGULAR");

        order.setProcessed(true);
        assertTrue(order.isProcessed());

        order.setProcessed(false);
        assertFalse(order.isProcessed());

        order.setProcessed(true);
        assertTrue(order.isProcessed());
    }

    @Test
    public void testOrderToStringContainsAllFields() {
        Order order = new Order("Полный тест", "URGENT");
        order.setProcessed(true);
        String toString = order.toString();

        assertTrue(toString.contains(order.getId()));
        assertTrue(toString.contains("Полный тест"));
        assertTrue(toString.contains("URGENT"));
        assertTrue(toString.contains(String.valueOf(true)));
    }

    @Test
    public void testOrderWithSpecialCharactersInDescription() {
        String specialDesc = "Заказ с !@#$%^&*()_+ спецсимволами и пробелами";
        Order order = new Order(specialDesc, "REGULAR");

        assertEquals("Описание со спецсимволами должно сохраниться", specialDesc, order.getDescription());
    }

    @Test
    public void testOrderWithUnicodeDescription() {
        String unicodeDesc = "Заказ на русском 🇷🇺 and English with emoji";
        Order order = new Order(unicodeDesc, "REGULAR");

        assertEquals("Unicode описание должно сохраниться", unicodeDesc, order.getDescription());
    }

    @Test
    public void testOrderCreatedAtCannotBeChangedByReference() {
        Order order = new Order("Тест", "REGULAR");
        LocalDateTime originalCreatedAt = order.getCreatedAt();

        order.setCreatedAt(LocalDateTime.now().minusDays(1));

        assertNotEquals("createdAt должен измениться после setter", originalCreatedAt, order.getCreatedAt());
    }

    @Test
    public void testOrderProcessedAtCanBeUpdated() {
        Order order = new Order("Тест", "REGULAR");
        LocalDateTime firstTime = LocalDateTime.now();
        LocalDateTime secondTime = firstTime.plusHours(1);

        order.setProcessedAt(firstTime);
        assertEquals(firstTime, order.getProcessedAt());

        order.setProcessedAt(secondTime);
        assertEquals(secondTime, order.getProcessedAt());
    }
}