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
package com.agorapulse.micronaut.log4aws.demo

import io.micronaut.context.ApplicationContext
import spock.lang.AutoCleanup
import spock.lang.Specification

class LoggingServiceSpec extends Specification {

    @AutoCleanup ApplicationContext context = ApplicationContext.builder().build().start()

    LoggingService service = context.getBean(LoggingService)

    void 'exception thrown'() {
        when:
            service.apply(
                trace: 'Testing Trace',
                debug: 'Testing Debug',
                warn: 'Testing Warning',
                error: 'Testing Error',
                throw: 'Testing Exception'
            )
        then:
            thrown(RuntimeException)
    }

}
