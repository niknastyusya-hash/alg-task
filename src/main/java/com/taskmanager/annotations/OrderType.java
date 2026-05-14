package com.taskmanager.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface OrderType {
    String value() default "REGULAR";

    enum Type {
        URGENT,
        REGULAR
    }
}
