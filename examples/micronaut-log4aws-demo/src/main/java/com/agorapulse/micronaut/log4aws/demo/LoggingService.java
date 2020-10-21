package com.agorapulse.micronaut.log4aws.demo;

import com.agorapulse.micronaut.log4aws.LogError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class LoggingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingService.class);

    @LogError
    String apply(Map<String, String> payload) {
        if (payload.containsKey("trace")) {
            LOGGER.trace(payload.get("trace"));
        }

        if (payload.containsKey("debug")) {
            LOGGER.debug(payload.get("debug"));
        }

        if (payload.containsKey("warn")) {
            LOGGER.warn(payload.get("warn"));
        }

        if (payload.containsKey("error")) {
            LOGGER.error(payload.get("error"), new RuntimeException(payload.get("error")));
        }

        if (payload.containsKey("env")) {
            LOGGER.debug(System.getenv().entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.joining("\n")));
        }

        if (payload.containsKey("throw")) {
            throw new RuntimeException(payload.get("throw"));
        }
        return "nothing to do";
    }

}
