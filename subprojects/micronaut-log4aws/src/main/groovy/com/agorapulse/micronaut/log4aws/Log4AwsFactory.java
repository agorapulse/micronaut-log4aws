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
import io.sentry.*;
import io.sentry.config.PropertiesProviderFactory;
import io.sentry.log4j2.SentryAppender;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Initializes Sentry for Micronaut.
 */
@Factory
public class Log4AwsFactory {

    /**
     * Automatically registers sentry logging during application context startup.
     *
     * @return sentry appender
     */
    @Bean
    @Context
    public SentryAppender sentryAppender(IHub hub) {
        LoggerContext lc = (LoggerContext) LogManager.getContext(false);
        Configuration configuration = lc.getConfiguration();

        String name = "Sentry";
        SentryAppender appender = null;

        for (Map.Entry<String, Appender> e : configuration.getAppenders().entrySet()) {
            if (e.getValue() instanceof SentryAppender) {
                appender = (SentryAppender) e.getValue();
                name = e.getKey();
            }
        }

        if (appender == null) {
            SentryOptions options = SentryOptions.from(PropertiesProviderFactory.create());
            appender = new SentryAppender(
                name,
                null,
                Optional.ofNullable(options.getDsn()).orElse(""),
                Level.INFO,
                Level.ERROR,
                options.getTransport(),
                hub);
            appender.start();
            configuration.addAppender(appender);
        }

        LoggerConfig rootLoggerConfig = configuration.getRootLogger();
        if (rootLoggerConfig.getAppenders().values().stream().noneMatch(SentryAppender.class::isInstance)) {
            rootLoggerConfig.removeAppender(name);
            rootLoggerConfig.addAppender(configuration.getAppender(name), Level.WARN, null);
        }

        lc.updateLoggers();

        return appender;
    }

    /**
     * Sentry client to be injected.
     * <p>
     * Please, use the injection instead of static reference to simplify testing.
     *
     * @return sentry client
     */
    @Bean(preDestroy = "close")
    @Context
    public IHub sentryHub(
        List<EventProcessor> eventProcessors,
        List<Integration> integrations,
        List<IScopeObserver> scopeObservers
    ) {
        SentryOptions propertiesOptions = SentryOptions.from(PropertiesProviderFactory.create());

        Optional.ofNullable(propertiesOptions.getDsn()).ifPresent(dsn -> Sentry.init(options -> {
            options.setEnableExternalConfiguration(true);
            options.setDsn(dsn);
            eventProcessors.forEach(options::addEventProcessor);
            integrations.forEach(options::addIntegration);
            scopeObservers.forEach(options::addScopeObserver);
        }));

        return HubAdapter.getInstance();
    }
}
