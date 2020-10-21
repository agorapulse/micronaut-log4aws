package com.agorapulse.micronaut.log4aws.demo;

import io.micronaut.function.FunctionBean;
import io.micronaut.function.executor.FunctionInitializer;

import javax.inject.Inject;
import java.util.Map;
import java.util.function.Function;

@FunctionBean(
    method = "apply",
    name = "logging-demo"
)
public class LoggingDemo  extends FunctionInitializer implements Function<Map<String, String>, String>  {

    @Inject private LoggingService loggingService;

    @Override
    public String apply(Map<String, String> payload) {
        return loggingService.apply(payload);
    }

}
