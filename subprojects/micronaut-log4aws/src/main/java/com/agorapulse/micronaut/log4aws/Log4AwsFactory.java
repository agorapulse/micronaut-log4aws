/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2022 Agorapulse.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.agorapulse.micronaut.log4aws;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.util.StringUtils;
import io.sentry.EventProcessor;
import io.sentry.IHub;
import io.sentry.Sentry;
import io.sentry.SentryOptions;
import io.sentry.log4j2.SentryAppender;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Initializes Sentry for Micronaut.
 */
@Factory
public class Log4AwsFactory {

    private static final String APPENDER_NAME = "Sentry";

    /**
     * Automatically registers sentry logging during application context startup.
     * @return sentry appender
     */
    @Bean
    @Context
    public SentryAppender sentryAppender(
        IHub hub,
        @Value("${sentry.dsn:}") String sentryDsn,
        @Value("${SENTRY_DSN:}") String sentryDsnLegacy
    ) {
        return initializeAppenderIfMissing(
            SentryAppender.class,
            Level.WARN,
            APPENDER_NAME,
            () -> new SentryAppender(
                APPENDER_NAME,
                null,
                StringUtils.isNotEmpty(sentryDsn) ? sentryDsn : sentryDsnLegacy,
                null,
                null,
                null,
                null,
                hub,
                null
            )
        );
    }

    /**
     * Sentry client to be injected.
     *
     * Please, use the injection instead of static reference to simplify testing.
     *
     * @return sentry client
     */
    @Bean
    @Context
    public IHub sentryClient(
        List<Sentry.OptionsConfiguration<SentryOptions>> configurations,
        @Value("${sentry.dsn:}") String sentryDsn,
        @Value("${SENTRY_DSN:}") String sentryDsnLegacy
    ) {
        Sentry.init(options -> {
            options.setDsn(
                StringUtils.isNotEmpty(sentryDsn) ? sentryDsn : sentryDsnLegacy
            );
            configurations.forEach(c -> c.configure(options));
        });
        return Sentry.getCurrentHub();
    }

    @Bean
    @Context
    public Sentry.OptionsConfiguration<SentryOptions> eventProcessors(List<EventProcessor> processors) {
        return options -> processors.forEach(options::addEventProcessor);
    }

    private <A extends Appender> A initializeAppenderIfMissing(
        Class<A> appenderClass,
        Level defaultLevel,
        String defaultName,
        Supplier<A> initializer
    ) {
        LoggerContext lc = (LoggerContext) LogManager.getContext(false);
        Configuration configuration = lc.getConfiguration();

        String name = defaultName;
        A appender = null;

        for (Map.Entry<String, Appender> e : configuration.getAppenders().entrySet()) {
            if (appenderClass.isInstance(e.getValue())) {
                appender = appenderClass.cast(e.getValue());
                name = e.getKey();
            }
        }

        if (appender == null) {
            appender = initializer.get();
            appender.start();
            configuration.addAppender(appender);
        }

        LoggerConfig rootLoggerConfig = configuration.getRootLogger();
        if (rootLoggerConfig.getAppenders().values().stream().noneMatch(appenderClass::isInstance)) {
            rootLoggerConfig.removeAppender(name);
            rootLoggerConfig.addAppender(configuration.getAppender(name), defaultLevel, null);
        }

        lc.updateLoggers();

        return appender;
    }

}
