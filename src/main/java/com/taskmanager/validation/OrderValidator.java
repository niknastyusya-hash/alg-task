package com.taskmanager.validation;

import com.taskmanager.annotations.OrderType;
import com.taskmanager.annotations.Validate;
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

        List<String> validationErrors = new ArrayList<>();
        Field[] fields = Order.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(Validate.class)) {
                Validate validate = field.getAnnotation(Validate.class);

                try {
                    Object value = field.get(order);

                    if (validate.required() && value == null) {
                        validationErrors.add(validate.message());
                    } else if (field.getType() == String.class && validate.required()) {
                        String strValue = (String) value;
                        if (strValue == null || strValue.trim().isEmpty()) {
                            validationErrors.add(validate.message());
                        }
                    }
                } catch (IllegalAccessException e) {
                    validationErrors.add("Cannot access field: " + field.getName());
                }
            }

            if (field.isAnnotationPresent(OrderType.class) && field.getType() == String.class) {
                try {
                    String typeValue = (String) field.get(order);
                    if (typeValue != null) {
                        boolean isValidType = typeValue.equals(OrderType.Type.URGENT.name()) ||
                                typeValue.equals(OrderType.Type.REGULAR.name());
                        if (!isValidType) {
                            validationErrors.add("Invalid order type: " + typeValue +
                                    ". Must be URGENT or REGULAR");
                        }
                    }
                } catch (IllegalAccessException e) {
                    validationErrors.add("Cannot validate order type: " + e.getMessage());
                }
            }
        }

        if (!validationErrors.isEmpty()) {
            logger.warning("Validation failed for order " + order.getId() + ": " + validationErrors);
            return false;
        }

        return true;
    }

    public boolean validateOrderId(String id) {
        return id != null && !id.trim().isEmpty();
    }

    public boolean validateDescription(String description) {
        return description != null && !description.trim().isEmpty() && description.length() <= 200;
    }
}
