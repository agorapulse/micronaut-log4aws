/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020 Vladimir Orany.
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
package com.agorapulse.micronaut.log4aws

import io.micronaut.context.ApplicationContext
import org.junit.Rule
import org.junit.contrib.java.lang.system.SystemOutRule
import spock.lang.AutoCleanup
import spock.lang.Specification
import javax.inject.Singleton

class LogErrorInterceptorSpec extends Specification {

    private static final String LOGGED_EVENT = 'ERROR ThrowsIllegalArgumentTester:44 - Exception executing method'

    @Rule SystemOutRule systemOutRule = new SystemOutRule().enableLog()

    @AutoCleanup ApplicationContext context = ApplicationContext.run()

    void 'exception is logged'() {
        given:
            ThrowsIllegalArgumentTester tester = context.getBean(ThrowsIllegalArgumentTester)
        when:
            tester.normal()
        then:
            thrown IllegalArgumentException

            systemOutRule.log.contains(LOGGED_EVENT)
    }

    void 'exception is muted'() {
        given:
            ThrowsIllegalArgumentTester tester = context.getBean(ThrowsIllegalArgumentTester)
        when:
            tester.muted()
        then:
            noExceptionThrown()

            systemOutRule.log.contains(LOGGED_EVENT)
    }

    void 'exception is filtered'() {
        given:
            ThrowsIllegalArgumentTester tester = context.getBean(ThrowsIllegalArgumentTester)
        when:
            tester.filtered()
        then:
            thrown IllegalArgumentException

            !systemOutRule.log.contains(LOGGED_EVENT)
    }

    void 'exception is filtered and muted match'() {
        given:
            ThrowsIllegalArgumentTester tester = context.getBean(ThrowsIllegalArgumentTester)
        when:
            tester.filteredAndMutedMatch()
        then:
            noExceptionThrown()

            systemOutRule.log.contains(LOGGED_EVENT)
    }

    void 'exception is filtered and muted mismatch'() {
        given:
            ThrowsIllegalArgumentTester tester = context.getBean(ThrowsIllegalArgumentTester)
        when:
            tester.filteredAndMutedMismatch()
        then:
            thrown IllegalArgumentException

            !systemOutRule.log.contains(LOGGED_EVENT)
    }

}

@Singleton
class ThrowsIllegalArgumentTester {

    @LogError
    void normal() {
        throw new IllegalArgumentException()
    }

    @LogError(mute = true)
    void muted() {
        throw new IllegalArgumentException()
    }

    @LogError(only = IllegalStateException)
    void filtered() {
        throw new IllegalArgumentException()
    }

    @LogError(mute = true, only = IllegalArgumentException)
    void filteredAndMutedMatch() {
        throw new IllegalArgumentException()
    }

    @LogError(mute = true, only = IllegalStateException)
    void filteredAndMutedMismatch() {
        throw new IllegalArgumentException()
    }

}
