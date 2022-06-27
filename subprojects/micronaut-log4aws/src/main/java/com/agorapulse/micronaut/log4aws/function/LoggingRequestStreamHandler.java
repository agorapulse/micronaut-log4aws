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
package com.agorapulse.micronaut.log4aws.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.InputStream;
import java.io.OutputStream;

public interface LoggingRequestStreamHandler<I, O> extends RequestStreamHandler {

    default void handleRequest(InputStream input, OutputStream output, Context context) {
        Logging.runAndRethrow(getClass(), "Exception running handler", () -> doHandleRequest(input, output, context));
    }

    <T> T doHandleRequest(InputStream input, OutputStream output, Context context);

}
