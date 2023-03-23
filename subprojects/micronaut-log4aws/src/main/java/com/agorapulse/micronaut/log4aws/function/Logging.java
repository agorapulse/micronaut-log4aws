/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2023 Agorapulse.
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

import io.sentry.Sentry;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class Logging {

    public static void runAndRethrow(Class<?> referenceClass, String message, Runnable action)  {
        try {
            action.run();
        } catch (Exception e) {
            LoggerFactory.getLogger(referenceClass).error(message, e);
            Sentry.flush(10000);
            throw new IllegalStateException(message, e);
        }
    }

    public static <T> T callAndRethrow(Class<?> referenceClass, String message, Callable<T> action)  {
        try {
            return action.call();
        } catch (Exception e) {
            LoggerFactory.getLogger(referenceClass).error(message, e);
            Sentry.flush(10000);
            throw new IllegalStateException(message, e);
        }
    }

}
