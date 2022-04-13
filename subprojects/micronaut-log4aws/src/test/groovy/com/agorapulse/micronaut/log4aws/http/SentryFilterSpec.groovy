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
package com.agorapulse.micronaut.log4aws.http

import com.agorapulse.gru.Gru
import com.agorapulse.gru.http.Http
import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.server.EmbeddedServer
import io.sentry.IHub
import spock.lang.AutoCleanup
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

class SentryFilterSpec extends Specification {

    @AutoCleanup Gru gru = Gru.create(Http.create(this))

    @AutoCleanup ApplicationContext context
    @AutoCleanup EmbeddedServer server

    IHub hub = Mock()

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

            1 * hub.pushScope()
            1 * hub.addBreadcrumb(_)
            1 * hub.configureScope(_)
            1 * hub.popScope()
    }

    void 'try error message'() {
        given:
            AtomicInteger pushCalls = new AtomicInteger()
            AtomicInteger breadcrumbsCalls = new AtomicInteger()
            AtomicInteger configureScopeCalls = new AtomicInteger()
            AtomicInteger popCalls = new AtomicInteger()
        when:
            gru.test {
                post('/test/parameter')
                expect {
                    status INTERNAL_SERVER_ERROR
                }
            }
        then:
            gru.verify()

            _ * hub.pushScope() >> {
                pushCalls.incrementAndGet()
            }
            _ * hub.addBreadcrumb(_)  >> {
                breadcrumbsCalls.incrementAndGet()
            }
            _ * hub.configureScope(_) >> {
                configureScopeCalls.incrementAndGet()
            }
            _ * hub.popScope() >> {
                popCalls.incrementAndGet()
            }

        expect:
            // the filter is only called once for Micronaut 3.x but twice for Micronaut 1.x and 2.x
            pushCalls.get() in 1..2
            pushCalls.get() == breadcrumbsCalls.get()
            pushCalls.get() == configureScopeCalls.get()
            pushCalls.get() == popCalls.get()
    }

    void 'try validation'() {
        given:
            AtomicInteger pushCalls = new AtomicInteger()
            AtomicInteger breadcrumbsCalls = new AtomicInteger()
            AtomicInteger configureScopeCalls = new AtomicInteger()
            AtomicInteger popCalls = new AtomicInteger()
        when:
            gru.test {
                put('/test/validated')
                expect {
                    status BAD_REQUEST
                }
            }
        then:
            gru.verify()

            _ * hub.pushScope() >> {
                pushCalls.incrementAndGet()
            }
            _ * hub.addBreadcrumb(_)  >> {
                breadcrumbsCalls.incrementAndGet()
            }
            _ * hub.configureScope(_) >> {
                configureScopeCalls.incrementAndGet()
            }
            _ * hub.popScope() >> {
                popCalls.incrementAndGet()
            }

        expect:
            // the filter is only called once for Micronaut 3.x but twice for Micronaut 1.x and 2.x
            pushCalls.get() in 1..2
            pushCalls.get() == breadcrumbsCalls.get()
            pushCalls.get() == configureScopeCalls.get()
            pushCalls.get() == popCalls.get()
    }

}
