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
package com.agorapulse.micronaut.log4aws.demo;

import com.agorapulse.micronaut.log4aws.LogError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class LoggingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingService.class);

    @LogError
    String apply(Map<String, String> payload) {
        if (payload.containsKey("trace")) {
            LOGGER.trace(payload.get("trace"));
        }

        if (payload.containsKey("debug")) {
            LOGGER.debug(payload.get("debug"));
        }

        if (payload.containsKey("warn")) {
            LOGGER.warn(payload.get("warn"));
        }

        if (payload.containsKey("error")) {
            LOGGER.error(payload.get("error"), new RuntimeException(payload.get("error")));
        }

        if (payload.containsKey("env")) {
            LOGGER.debug(System.getenv().entrySet().stream().map(e -> e.getKey() + ": " + e.getValue()).collect(Collectors.joining("\n")));
        }

        if (payload.containsKey("throw")) {
            throw new RuntimeException(payload.get("throw"));
        }
        return "nothing to do";
    }

}
