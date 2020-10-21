/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020 Agorapulse.
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
import io.sentry.Sentry;
import io.sentry.SentryClient;
import io.sentry.config.Lookup;
import io.sentry.connection.EventSendCallback;
import io.sentry.dsn.Dsn;
import io.sentry.event.Event;
import io.sentry.log4j2.SentryAppender;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

import static io.sentry.DefaultSentryClientFactory.ASYNC_OPTION;

/**
 * Initializes Sentry for Micronaut.
 */
@Factory
public class SentryClientFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(SentryClientFactory.class);
    private static final String APPENDER_NAME = "Sentry";

    /**
     * Automatically registers sentry logging during application context startup.
     * @return sentry appender
     */
    @Bean
    @Context
    public SentryAppender sentryAppender() {
        boolean sync = !Boolean.FALSE.toString().equals(Lookup.getDefault().get(ASYNC_OPTION));
        boolean dsnProvided = !Dsn.DEFAULT_DSN.equals(Dsn.dsnLookup());

        if (sync && dsnProvided) {
            // in future releases
            // throw new IllegalStateException("Sentry not configured correctly for synchornous calls! Please, create file 'sentry.properties' and add there a line 'async=false'");
            LOGGER.error("Sentry not configured correctly for synchornous calls! Please, create file 'sentry.properties' and add there a line 'async=false'");
        }

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


        LoggerContext lc = (LoggerContext) LogManager.getContext(false);
        Configuration configuration = lc.getConfiguration();

        SentryAppender appender = configuration.getAppender(APPENDER_NAME);
        if (appender == null) {
            appender = new SentryAppender();
            appender.start();
        }
        configuration.addAppender(appender);

        LoggerConfig rootLoggerConfig = configuration.getRootLogger();
        rootLoggerConfig.removeAppender(APPENDER_NAME);
        rootLoggerConfig.addAppender(configuration.getAppender(APPENDER_NAME), Level.WARN, null);
        lc.updateLoggers();

        return appender;
    }

    /**
     * Sentry client to be injected.
     *
     * Please, use the injection instead of static reference to simplify testing.
     *
     * @return sentry client
     */
    @Bean
    @Singleton
    public SentryClient sentryClient() {
        return Sentry.getStoredClient();
    }

}
