/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2025 Agorapulse.
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
import spock.lang.AutoCleanup
import spock.lang.Specification
import jakarta.inject.Singleton

import static com.github.stefanbirkner.systemlambda.SystemLambda.*

/**
 * Tests the log error interceptor.
 */
class LogErrorInterceptorSpec extends Specification {

    private static final String LOGGED_EVENT = 'ERROR ThrowsIllegalArgumentTester:45 - Exception executing method'

    @AutoCleanup ApplicationContext context = ApplicationContext.run()

    void 'exception is logged'() {
        given:
            ThrowsIllegalArgumentTester tester = context.getBean(ThrowsIllegalArgumentTester)
            IllegalArgumentException ex
        when:
            String out = tapSystemErrAndOut {
                try {
                    tester.normal()
                } catch (IllegalArgumentException e) {
                    ex = e
                }
            }
        then:
            ex
            out?.contains(LOGGED_EVENT)
    }

    void 'exception is muted'() {
        given:
            ThrowsIllegalArgumentTester tester = context.getBean(ThrowsIllegalArgumentTester)
        when:
            String out = tapSystemErrAndOut {
                tester.muted()
            }
        then:
            noExceptionThrown()

            out?.contains(LOGGED_EVENT)
    }

    void 'exception is filtered'() {
        given:
            ThrowsIllegalArgumentTester tester = context.getBean(ThrowsIllegalArgumentTester)
        when:
            String out = tapSystemErrAndOut {
                tester.filtered()
            }
        then:
            thrown IllegalArgumentException

            !out?.contains(LOGGED_EVENT)
    }

    void 'exception is filtered and muted match'() {
        given:
            ThrowsIllegalArgumentTester tester = context.getBean(ThrowsIllegalArgumentTester)
        when:
            String out = tapSystemErrAndOut {
                tester.filteredAndMutedMatch()
            }
        then:
            noExceptionThrown()

            out?.contains(LOGGED_EVENT)
    }

    void 'exception is filtered and muted mismatch'() {
        given:
            ThrowsIllegalArgumentTester tester = context.getBean(ThrowsIllegalArgumentTester)
        when:
            String out = tapSystemErrAndOut {
                tester.filteredAndMutedMismatch()
            }
        then:
            thrown IllegalArgumentException

            !out?.contains(LOGGED_EVENT)
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
