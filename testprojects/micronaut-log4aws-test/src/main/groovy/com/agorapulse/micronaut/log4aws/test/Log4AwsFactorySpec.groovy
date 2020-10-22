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
package com.agorapulse.micronaut.log4aws.test

import io.micronaut.context.ApplicationContext
import io.sentry.SentryClient
import io.sentry.log4j2.SentryAppender
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.LoggerContext
import org.apache.logging.log4j.core.config.Configuration
import org.apache.logging.log4j.core.config.LoggerConfig
import spock.lang.AutoCleanup
import spock.lang.Specification

@SuppressWarnings('AbstractClassWithoutAbstractMethod')
abstract class Log4AwsFactorySpec extends Specification {

    @AutoCleanup ApplicationContext context = ApplicationContext.run()

    void 'sentry appender configured'() {
        expect:
            context.getBean(SentryClient)
            context.getBean(SentryAppender)

        when:
            LoggerContext lc = (LoggerContext) LogManager.getContext(false)
            Configuration configuration = lc.configuration
        then:
            configuration.appenders.values().any { it instanceof SentryAppender }

        when:
            LoggerConfig rootLoggerConfig = configuration.rootLogger
        then:
            rootLoggerConfig.appenders.values().any { it instanceof SentryAppender }
    }

}
