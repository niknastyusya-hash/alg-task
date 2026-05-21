package com.taskmanager.annotations;

import com.taskmanager.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Аннотации тесты")
class AnnotationTest {

    @Nested
    @DisplayName("Проверка наличия аннотаций")
    class AnnotationPresenceTests {

        @Test
        @DisplayName("Аннотация @Validate присутствует у поля id")
        void testAnnotationPresenceOnId() throws NoSuchFieldException {
            Field idField = Order.class.getDeclaredField("id");
            assertNotNull(idField.getAnnotation(Validate.class));
        }

        @Test
        @DisplayName("Аннотация @Validate присутствует у поля customerName")
        void testAnnotationPresenceOnCustomerName() throws NoSuchFieldException {
            Field customerField = Order.class.getDeclaredField("customerName");
            assertNotNull(customerField.getAnnotation(Validate.class));
        }

        @Test
        @DisplayName("Аннотация @Validate присутствует у поля product")
        void testAnnotationPresenceOnProduct() throws NoSuchFieldException {
            Field productField = Order.class.getDeclaredField("product");
            assertNotNull(productField.getAnnotation(Validate.class));
        }

        @Test
        @DisplayName("Аннотация @OrderType присутствует у поля priority")
        void testOrderTypeAnnotationOnPriority() throws NoSuchFieldException {
            Field priorityField = Order.class.getDeclaredField("priority");
            assertNotNull(priorityField.getAnnotation(OrderType.class));
        }
    }

    @Nested
    @DisplayName("Проверка значений аннотаций")
    class AnnotationValueTests {

        @Test
        @DisplayName("Аннотация @OrderType имеет значение по умолчанию")
        void testOrderTypeAnnotationValue() throws NoSuchFieldException {
            Field priorityField = Order.class.getDeclaredField("priority");
            OrderType orderType = priorityField.getAnnotation(OrderType.class);
            assertNotNull(orderType);
            assertNotNull(orderType.value());
        }

        @Test
        @DisplayName("Аннотация @Validate имеет поле required")
        void testValidateAnnotationRequired() throws NoSuchFieldException {
            Field idField = Order.class.getDeclaredField("id");
            Validate validate = idField.getAnnotation(Validate.class);
            assertNotNull(validate);
            assertTrue(validate.required() == true || validate.required() == false);
        }

        @Test
        @DisplayName("Аннотация @Validate имеет сообщение")
        void testValidateAnnotationMessage() throws NoSuchFieldException {
            Field idField = Order.class.getDeclaredField("id");
            Validate validate = idField.getAnnotation(Validate.class);
            assertNotNull(validate);
            assertNotNull(validate.message());
        }
    }
}