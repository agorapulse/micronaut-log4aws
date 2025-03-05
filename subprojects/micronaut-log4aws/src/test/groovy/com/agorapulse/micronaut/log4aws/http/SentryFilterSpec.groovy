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
package com.agorapulse.micronaut.log4aws.http

import com.agorapulse.gru.Gru
import com.agorapulse.gru.http.Http
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import io.sentry.IHub
import io.sentry.Scope
import spock.lang.AutoCleanup
import spock.lang.Specification

class SentryFilterSpec extends Specification {

    @AutoCleanup Gru gru = Gru.create(Http.create(this))

    @AutoCleanup ApplicationContext context
    @AutoCleanup EmbeddedServer server

    IHub hub = new MockHub()

    void setup() {
        context = ApplicationContext.builder().build()
        context.registerSingleton(IHub, hub)

        context.start()

        server = context.getBean(EmbeddedServer)
        server.start()

        gru.prepare(server.URL.toString())
    }

    void 'try ok message'() {
        when:
            gru.test {
                get('/test/parameter')
                expect {
                    text inline('parameter')
                }
            }
        then:
            gru.verify()

            hub.pointer == -1
            hub.scopes.size() == 1

        when:
            Scope scope = hub.scopes[0]
        then:
            scope.breadcrumbs.size() == 1
            scope.eventProcessors.size() == 1
    }

    void 'try error message'() {
        when:
            gru.test {
                post('/test/parameter')
                expect {
                    status INTERNAL_SERVER_ERROR
                }
            }
        then:
            gru.verify()

            hub.pointer == -1
            // the filter is only called once for Micronaut 3.x but twice for Micronaut 1.x and 2.x
            hub.scopes.size() in 1..2

        when:
            Scope scope = hub.scopes[0]
        then:
            scope.breadcrumbs.size() in 1
            scope.eventProcessors.size() == 1
    }

    void 'try validation'() {
        when:
            gru.test {
                put('/test/validated')
                expect {
                    status BAD_REQUEST
                }
            }
        then:
            gru.verify()

            hub.pointer == -1
            // the filter is only called once for Micronaut 3.x but twice for Micronaut 1.x and 2.x
            hub.scopes.size() in 1..2

        when:
            Scope scope = hub.scopes[0]
        then:
            scope.breadcrumbs.size() in 1
            scope.eventProcessors.size() == 1
    }

}
