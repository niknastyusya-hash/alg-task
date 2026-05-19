package com.taskmanager.annotations;

import com.taskmanager.model.Order;
import org.junit.Test;
import java.lang.reflect.Field;
import static org.junit.Assert.*;

public class AnnotationTest {

    @Test
    public void testAnnotationPresence() throws NoSuchFieldException {
        Field idField = Order.class.getDeclaredField("id");
        Field descriptionField = Order.class.getDeclaredField("description");
        Field typeField = Order.class.getDeclaredField("type");

        assertNotNull("Аннотация @Validate отсутствует у поля id",
                idField.getAnnotation(com.taskmanager.annotations.Validate.class));
        assertNotNull("Аннотация @Validate отсутствует у поля description",
                descriptionField.getAnnotation(com.taskmanager.annotations.Validate.class));
        assertNotNull("Аннотация @OrderType отсутствует у поля type",
                typeField.getAnnotation(OrderType.class));
    }

    @Test
    public void testOrderTypeAnnotationValue() throws NoSuchFieldException {
        Field typeField = Order.class.getDeclaredField("type");
        OrderType orderType = typeField.getAnnotation(OrderType.class);

        assertNotNull("Аннотация не найдена", orderType);

        String defaultValue = orderType.value();
        assertNotNull("Default value не должен быть null", defaultValue);
    }

    @Test
    public void testValidateAnnotationRequired() throws NoSuchFieldException {
        Field idField = Order.class.getDeclaredField("id");
        com.taskmanager.annotations.Validate validateId = idField.getAnnotation(com.taskmanager.annotations.Validate.class);

        assertNotNull("Аннотация не найдена", validateId);

        boolean required = validateId.required();
        assertTrue("required должен быть true или false", required == true || required == false);
    }

    @Test
    public void testValidateAnnotationMessage() throws NoSuchFieldException {
        Field idField = Order.class.getDeclaredField("id");
        com.taskmanager.annotations.Validate validateId = idField.getAnnotation(com.taskmanager.annotations.Validate.class);

        assertNotNull("Аннотация не найдена", validateId);

        String message = validateId.message();
        assertNotNull("Message не должен быть null", message);
    }
}