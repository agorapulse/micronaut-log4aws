package com.agorapulse.micronaut.log4aws;

import io.micronaut.aop.Around;
import io.micronaut.context.annotation.Type;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Around
@Type(value = LogExceptionsInterceptor.class)
public @interface LogError {

    boolean mute() default false;
    Class<? extends Throwable> only() default Throwable.class;

}
