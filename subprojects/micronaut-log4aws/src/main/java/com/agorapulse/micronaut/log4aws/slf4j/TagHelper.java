package com.agorapulse.micronaut.log4aws.slf4j;

import org.slf4j.Logger;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

public class TagHelper {

    public static void setTags(Map<String, String> tags) {
        tags.forEach(MDC::put);
    }

    public static void clearTags(Map<String, String> tags) {
        tags.keySet().forEach(MDC::remove);
    }

    public static <T> T withTags(Map<String, String> tags, Callable<T> callable) {
        setTags(tags);
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            clearTags(tags);
        }
    }

    public static void withTags(Map<String, String> tags, Runnable callable) {
        setTags(tags);
        try {
            callable.run();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            clearTags(tags);
        }
    }

    public static Logger withTags(Map<String, String> tags, Logger logger) {
        return new LoggerWithTags(logger, tags);
    }

}
