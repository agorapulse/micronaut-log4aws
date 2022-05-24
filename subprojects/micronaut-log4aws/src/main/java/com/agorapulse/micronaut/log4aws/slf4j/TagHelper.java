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
package com.agorapulse.micronaut.log4aws.slf4j;

import org.slf4j.Logger;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.Callable;

public class TagHelper {

    public static void setTags(Map<String, String> tags) {
        tags.forEach(MDC::put);
    }

    public static void clearTags(Map<String, String> tags) {
        tags.keySet().forEach(MDC::remove);
    }

    public static <T> T withTags(Map<String, String> tags, Callable<T> callable) {
        setTags(tags);
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            clearTags(tags);
        }
    }

    public static void withTags(Map<String, String> tags, Runnable callable) {
        setTags(tags);
        try {
            callable.run();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            clearTags(tags);
        }
    }

    public static Logger withTags(Map<String, String> tags, Logger logger) {
        return new LoggerWithTags(logger, tags);
    }

}
