package com.agorapulse.micronaut.log4aws.function;

import io.micronaut.context.ApplicationContext;
import io.micronaut.function.executor.FunctionInitializer;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

public class LoggingFunctionInitializer extends FunctionInitializer {

    public LoggingFunctionInitializer() {
        // for AWS startup
        super();
    }

    public LoggingFunctionInitializer(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    public LoggingFunctionInitializer(ApplicationContext applicationContext, boolean inject) {
        super(applicationContext, inject);
    }

    @Override
    protected void startThis(ApplicationContext applicationContext) {
        try {
            super.startThis(applicationContext);
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error("Exception starting the application context", e);
            throw e;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ApplicationContext buildApplicationContext(@Nullable Object context) {
        try {
            return super.buildApplicationContext(context);
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error("Exception building the application context", e);
            throw e;
        }
    }

    @Override
    protected void injectThis(ApplicationContext applicationContext) {
        try {
            super.injectThis(applicationContext);
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error("Exception injecting the function handler", e);
            throw e;
        }
    }
}
