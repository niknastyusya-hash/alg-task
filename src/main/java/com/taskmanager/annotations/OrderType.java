package com.taskmanager.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Documented
public @interface OrderType {

    enum Priority {
        URGENT,
        NORMAL
    }

    Priority value() default Priority.NORMAL;

    String message() default "";
}
