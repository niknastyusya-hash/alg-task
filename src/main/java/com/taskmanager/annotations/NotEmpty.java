package com.taskmanager.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface NotEmpty {
    String message() default "Field cannot be empty";
}
