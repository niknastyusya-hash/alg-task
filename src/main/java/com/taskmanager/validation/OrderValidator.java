package com.taskmanager.validation;

import com.taskmanager.annotations.*;
import com.taskmanager.model.Order;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class OrderValidator {

    private static final Logger logger = Logger.getLogger(OrderValidator.class.getName());

    public boolean validate(Order order) {
        if (order == null) {
            logger.warning("Order object is null");
            return false;
        }

        List<String> errors = new ArrayList<>();
        Field[] fields = Order.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            try {
                Object value = field.get(order);

                if (field.isAnnotationPresent(NotNull.class)) {
                    NotNull annotation = field.getAnnotation(NotNull.class);
                    if (value == null) {
                        errors.add(annotation.message());
                    }
                }

                if (field.isAnnotationPresent(NotEmpty.class) && field.getType() == String.class) {
                    NotEmpty annotation = field.getAnnotation(NotEmpty.class);
                    String strValue = (String) value;
                    if (strValue == null || strValue.trim().isEmpty()) {
                        errors.add(annotation.message());
                    }
                }

                if (field.isAnnotationPresent(Validate.class)) {
                    Validate validate = field.getAnnotation(Validate.class);
                    if (validate.required() && value == null) {
                        errors.add(validate.message());
                    } else if (field.getType() == String.class && validate.required()) {
                        String strValue = (String) value;
                        if (strValue == null || strValue.trim().isEmpty()) {
                            errors.add(validate.message());
                        }
                    }
                }

                if (field.isAnnotationPresent(OrderType.class) && field.getType() == OrderType.Priority.class) {
                    if (value == null) {
                        errors.add("Order priority cannot be null");
                    }
                }

            } catch (IllegalAccessException e) {
                errors.add("Cannot access field: " + field.getName());
            }
        }

        if (!errors.isEmpty()) {
            logger.warning(String.format("Validation failed for order %s: %s",
                    order.getId(), errors));
            return false;
        }

        return true;
    }

    public static void validateStatic(Order order) {
        OrderValidator validator = new OrderValidator();
        if (!validator.validate(order)) {
            throw new IllegalArgumentException("Order validation failed");
        }
    }
}