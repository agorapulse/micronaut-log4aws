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
package com.agorapulse.micronaut.log4aws.demo;

import io.micronaut.function.FunctionBean;
import io.micronaut.function.executor.FunctionInitializer;

import jakarta.inject.Inject;
import java.util.Map;
import java.util.function.Function;

@FunctionBean(
    method = "apply",
    name = "logging-demo"
)
public class LoggingDemo  extends FunctionInitializer implements Function<Map<String, String>, String>  {

    @Inject private LoggingService loggingService;

    @Override
    public String apply(Map<String, String> payload) {
        return loggingService.apply(payload);
    }

}
