package com.taskmanager.validation;

import com.taskmanager.annotations.OrderType;
import com.taskmanager.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderValidator тесты")
class OrderValidatorTest {

    private OrderValidator validator;

    @BeforeEach
    void setUp() {
        validator = new OrderValidator();
    }

    @Nested
    @DisplayName("Валидные заказы")
    class ValidOrderTests {

        @Test
        @DisplayName("Валидный заказ проходит проверку")
        void testValidateWithAllValidFields() {
            Order order = new Order("John", "Laptop", OrderType.Priority.NORMAL);
            assertTrue(validator.validate(order));
        }

        @Test
        @DisplayName("Срочный заказ проходит проверку")
        void testValidateWithUrgentOrder() {
            Order order = new Order("Jane", "Phone", OrderType.Priority.URGENT);
            assertTrue(validator.validate(order));
        }
    }

    @Nested
    @DisplayName("Null объект")
    class NullObjectTests {

        @Test
        @DisplayName("Null заказ не проходит проверку")
        void testValidateWithNullOrder() {
            assertFalse(validator.validate(null));
        }
    }

    @Nested
    @DisplayName("@NotNull валидация")
    class NotNullTests {

        @Test
        @DisplayName("Null ID не проходит проверку")
        void testValidateWithNullId() {
            Order order = new Order("John", "Product", OrderType.Priority.NORMAL);
            order.setId(null);
            assertFalse(validator.validate(order));
        }

        @Test
        @DisplayName("Null customerName не проходит проверку")
        void testValidateWithNullCustomerName() {
            Order order = new Order(null, "Product", OrderType.Priority.NORMAL);
            assertFalse(validator.validate(order));
        }

        @Test
        @DisplayName("Null product не проходит проверку")
        void testValidateWithNullProduct() {
            Order order = new Order("John", null, OrderType.Priority.NORMAL);
            assertFalse(validator.validate(order));
        }

        @Test
        @DisplayName("Null priority не проходит проверку")
        void testValidateWithNullPriority() {
            Order order = new Order("John", "Product", null);
            assertFalse(validator.validate(order));
        }
    }

    @Nested
    @DisplayName("@NotEmpty валидация")
    class NotEmptyTests {

        @Test
        @DisplayName("Пустой customerName не проходит проверку")
        void testValidateWithEmptyCustomerName() {
            Order order = new Order("", "Product", OrderType.Priority.NORMAL);
            assertFalse(validator.validate(order));
        }

        @Test
        @DisplayName("Пустой product не проходит проверку")
        void testValidateWithEmptyProduct() {
            Order order = new Order("John", "", OrderType.Priority.NORMAL);
            assertFalse(validator.validate(order));
        }

        @Test
        @DisplayName("Пробелы в customerName не проходят")
        void testValidateWithWhitespaceCustomerName() {
            Order order = new Order("   ", "Product", OrderType.Priority.NORMAL);
            assertFalse(validator.validate(order));
        }

        @Test
        @DisplayName("Пробелы в product не проходят")
        void testValidateWithWhitespaceProduct() {
            Order order = new Order("John", "   ", OrderType.Priority.NORMAL);
            assertFalse(validator.validate(order));
        }
    }

    @Nested
    @DisplayName("@OrderType валидация")
    class OrderTypeValidationTests {

        @Test
        @DisplayName("Заказ с невалидным типом не проходит")
        void testValidateWithInvalidOrderType() {
            Order order = new Order("John", "Product", null);
            assertFalse(validator.validate(order));
        }
    }

    @Nested
    @DisplayName("Множественные ошибки")
    class MultipleErrorsTests {

        @Test
        @DisplayName("Заказ с несколькими невалидными полями")
        void testValidateMultipleInvalidFields() {
            Order order = new Order(null, null, null);
            order.setId(null);
            assertFalse(validator.validate(order));
        }
    }

    @Nested
    @DisplayName("Статический метод validateStatic")
    class StaticValidateTests {

        @Test
        @DisplayName("validateStatic выбрасывает исключение для невалидного заказа")
        void testValidateStaticThrowsException() {
            Order invalidOrder = new Order(null, null, OrderType.Priority.NORMAL);
            assertThrows(IllegalArgumentException.class,
                    () -> OrderValidator.validateStatic(invalidOrder));
        }

        @Test
        @DisplayName("validateStatic не выбрасывает исключение для валидного заказа")
        void testValidateStaticWithValidOrder() {
            Order validOrder = new Order("John", "Product", OrderType.Priority.NORMAL);
            assertDoesNotThrow(() -> OrderValidator.validateStatic(validOrder));
        }
    }
}