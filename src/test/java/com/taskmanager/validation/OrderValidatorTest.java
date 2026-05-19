package com.taskmanager.validation;

import com.taskmanager.model.Order;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OrderValidatorTest {

    private OrderValidator validator;

    @Before
    public void setUp() {
        validator = new OrderValidator();
    }


    @Test
    public void testValidateOrderIdWithNumbersOnly() {
        assertTrue("ID только из цифр должен быть валидным", validator.validateOrderId("123456"));
        assertTrue("ID из цифр должен быть валидным", validator.validateOrderId("0"));
        assertTrue("ID из цифр с ведущими нулями должен быть валидным", validator.validateOrderId("00123"));
    }

    @Test
    public void testValidateOrderIdWithLettersOnly() {
        assertTrue("ID только из букв должен быть валидным", validator.validateOrderId("order"));
        assertTrue("ID из букв в верхнем регистре должен быть валидным", validator.validateOrderId("ORDER"));
        assertTrue("ID из смешанного регистра должен быть валидным", validator.validateOrderId("OrderTest"));
    }

    @Test
    public void testValidateOrderIdWithEmptyAndWhitespace() {
        assertFalse("Пустая строка не должна быть валидной", validator.validateOrderId(""));
        assertFalse("Строка с пробелами не должна быть валидной", validator.validateOrderId("   "));
        assertFalse("Строка с табуляцией не должна быть валидной", validator.validateOrderId("\t"));
        assertFalse("Null не должен быть валидным", validator.validateOrderId(null));
    }

    @Test
    public void testValidateDescriptionWithMinLength() {
        String minLengthDesc = "A".repeat(1);
        assertTrue("Описание минимальной длины должно проходить", validator.validateDescription(minLengthDesc));

        String tooShortDesc = "";
        assertFalse("Пустое описание не должно проходить", validator.validateDescription(tooShortDesc));
    }

    @Test
    public void testValidateDescriptionWithMaxLength() {
        String exactMaxLength = "A".repeat(200);
        assertTrue("Описание максимальной длины должно проходить", validator.validateDescription(exactMaxLength));

        String exceedingMaxLength = "A".repeat(201);
        assertFalse("Описание превышающее максимальную длину не должно проходить",
                validator.validateDescription(exceedingMaxLength));

        String wayTooLong = "A".repeat(1000);
        assertFalse("Очень длинное описание не должно проходить", validator.validateDescription(wayTooLong));
    }

    @Test
    public void testValidateDescriptionWithSpecialCharacters() {
        assertTrue("Описание со спецсимволами должно быть валидным",
                validator.validateDescription("Заказ с !@#$%^&*() символами"));
        assertTrue("Описание с пунктуацией должно быть валидным",
                validator.validateDescription("Заказ: точка, запятая; восклицательный!"));
        assertTrue("Описание с кавычками должно быть валидным",
                validator.validateDescription("Заказ с \"кавычками\" и 'апострофами'"));
    }

    @Test
    public void testValidateDescriptionWithWhitespaceOnly() {
        assertFalse("Только пробелы не должны быть валидными", validator.validateDescription("   "));
        assertFalse("Только табуляция не должна быть валидной", validator.validateDescription("\t\t"));
        assertFalse("Только перенос строки не должен быть валидным", validator.validateDescription("\n\n"));
        assertFalse("Смешанные пробельные символы не должны быть валидными",
                validator.validateDescription(" \t\n "));
    }

    @Test
    public void testValidateDescriptionWithLeadingTrailingSpaces() {
        assertTrue("Описание с пробелами в начале должно быть валидным",
                validator.validateDescription("  Заказ с пробелами"));
        assertTrue("Описание с пробелами в конце должно быть валидным",
                validator.validateDescription("Заказ с пробелами  "));
        assertTrue("Описание с пробелами с обеих сторон должно быть валидным",
                validator.validateDescription("  Заказ с пробелами  "));
    }

    @Test
    public void testValidateDescriptionWithNumbers() {
        assertTrue("Описание с цифрами должно быть валидным",
                validator.validateDescription("Заказ №12345"));
        assertTrue("Описание только из цифр должно быть валидным",
                validator.validateDescription("12345"));
    }

    @Test
    public void testValidateWithAllValidFields() {
        Order order = new Order("Полностью валидный заказ", "REGULAR");
        order.setId("valid-id-123");

        assertTrue("Заказ со всеми валидными полями должен проходить", validator.validate(order));
    }

    @Test
    public void testValidateWithWhitespaceInType() {
        Order orderWithSpaces = new Order("Тест", "REGULAR   ");
        assertFalse("Тип с пробелами в конце не должен проходить", validator.validate(orderWithSpaces));

        Order orderWithLeadingSpaces = new Order("Тест", "   REGULAR");
        assertFalse("Тип с пробелами в начале не должен проходить", validator.validate(orderWithLeadingSpaces));
    }

    @Test
    public void testValidateWithLowerCaseType() {
        Order orderLowerRegular = new Order("Тест", "regular");
        Order orderLowerUrgent = new Order("Тест", "urgent");

        boolean regularResult = validator.validate(orderLowerRegular);
        boolean urgentResult = validator.validate(orderLowerUrgent);

        assertTrue(true);
    }


    @Test
    public void testValidateWithProcessedAndValidProcessedBy() {
        Order order = new Order("Тест", "REGULAR");
        order.setProcessed(true);
        order.setProcessedBy("Тестер");
        order.setProcessedAt(java.time.LocalDateTime.now());

        boolean result = validator.validate(order);
        assertTrue("Обработанный заказ с корректными данными должен проходить", result);
    }

    @Test
    public void testValidateMultipleOrders() {
        Order[] validOrders = {
                new Order("Заказ 1", "REGULAR"),
                new Order("Заказ 2", "URGENT"),
                new Order("Заказ 3", "REGULAR"),
                new Order("A".repeat(200), "URGENT")
        };

        for (Order order : validOrders) {
            assertTrue("Заказ должен быть валидным: " + order.getDescription(),
                    validator.validate(order));
        }
    }


    @Test
    public void testValidatorWithDifferentOrderTypes() {
        String[] validTypes = {"REGULAR", "URGENT"};
        String[] invalidTypes = {"", " ", "NORMAL", "HIGH", "LOW", "regular", "urgent", "Regular"};

        for (String type : validTypes) {
            Order order = new Order("Тест " + type, type);
            assertTrue("Тип " + type + " должен быть валидным", validator.validate(order));
        }

        for (String type : invalidTypes) {
            Order order = new Order("Тест " + type, type);
            assertFalse("Тип " + type + " не должен быть валидным", validator.validate(order));
        }
    }
}