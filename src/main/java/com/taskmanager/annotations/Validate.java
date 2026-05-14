package com.taskmanager.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Validate {
    boolean required() default true;
    String message() default "Field validation failed";
}