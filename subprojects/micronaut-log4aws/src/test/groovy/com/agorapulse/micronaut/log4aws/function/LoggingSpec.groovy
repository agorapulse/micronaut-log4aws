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
package com.agorapulse.micronaut.log4aws.function

import com.amazonaws.services.lambda.runtime.Context
import groovy.transform.CompileStatic
import spock.lang.Specification

class LoggingSpec extends Specification {

    void 'consumer test'() {
        when:
            new TestConsumer().accept('something')
        then:
            thrown(IllegalStateException)
    }

    void 'function test'() {
        when:
            new TestFunction().apply('something')
        then:
            thrown(IllegalStateException)
    }

    void 'request handler test'() {
        when:
            new TestRequestHandler().handleRequest('something', Mock(Context))
        then:
            thrown(IllegalStateException)
    }

    void 'request stream handler test'() {
        when:
            new TestRequestStreamHandler().handleRequest(Mock(InputStream), Mock(OutputStream), Mock(Context))
        then:
            thrown(IllegalStateException)
    }

}

@CompileStatic
class TestConsumer implements LoggingConsumer<String> {

    @Override
    void doAccept(String s) {
        throw new UnsupportedOperationException('not implemented')
    }

}

@CompileStatic
class TestFunction implements LoggingFunction<String, String> {

    @Override
    String doApply(String s) {
        throw new UnsupportedOperationException('not implemented')
    }

}

@CompileStatic
class TestRequestHandler implements LoggingRequestHandler<String, String> {

    @Override
    String doHandleRequest(String input, Context context) {
        throw new UnsupportedOperationException('not implemented')
    }

}

@CompileStatic
class TestRequestStreamHandler implements LoggingRequestStreamHandler {

    @Override
    public <T> T doHandleRequest(InputStream input, OutputStream output, Context context) {
        throw new UnsupportedOperationException('not implemented')
    }

}
