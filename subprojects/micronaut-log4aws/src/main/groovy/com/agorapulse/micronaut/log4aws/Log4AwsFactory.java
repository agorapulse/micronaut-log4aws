/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2021 Agorapulse.
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
import io.micronaut.context.env.Environment;
import io.sentry.Sentry;
import io.sentry.SentryClient;
import io.sentry.config.Lookup;
import io.sentry.connection.EventSendCallback;
import io.sentry.dsn.Dsn;
import io.sentry.event.Event;
import io.sentry.log4j2.SentryAppender;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Supplier;

import static io.sentry.DefaultSentryClientFactory.ASYNC_OPTION;

/**
 * Initializes Sentry for Micronaut.
 */
@Factory
public class Log4AwsFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(Log4AwsFactory.class);

    /**
     * Automatically registers sentry logging during application context startup.
     * @return sentry appender
     */
    @Bean
    @Context
    public SentryAppender sentryAppender(Environment environment) {
        boolean async = !Boolean.FALSE.toString().equals(Lookup.getDefault().get(ASYNC_OPTION));
        boolean dsnProvided = !Dsn.DEFAULT_DSN.equals(Dsn.dsnLookup());
        boolean function = environment.getActiveNames().contains(Environment.FUNCTION);

        if (async && dsnProvided && function) {
            // in future releases
            // throw new IllegalStateException("Sentry not configured correctly for synchornous calls! Please, create file 'sentry.properties' and add there a line 'async=false'");
            LOGGER.error("Sentry not configured correctly for synchronous calls! Please, create file 'sentry.properties' and add there a line 'async=false'");
        }

        return initializeAppenderIfMissing(
            SentryAppender.class,
            Level.WARN,
            SentryAppender.APPENDER_NAME,
            SentryAppender::new
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
    public SentryClient sentryClient() {
        Sentry.init();

        SentryClient client = Sentry.getStoredClient();
        client.addBuilderHelper(new AwsLambdaEventBuildHelper());
        client.addEventSendCallback(new EventSendCallback() {
            @Override
            public void onFailure(Event event, Exception exception) {
                LOGGER.error("Failed to send event to Sentry: " + event, exception);
            }

            @Override
            public void onSuccess(Event event) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("Event sent to Sentry: " + event);
                }
            }
        });

        return client;
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
