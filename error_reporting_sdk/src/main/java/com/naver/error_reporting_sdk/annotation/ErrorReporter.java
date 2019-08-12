package com.naver.error_reporting_sdk.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface ErrorReporter {
    String company() default "";

    String name() default "";

    String email() default "";
}
