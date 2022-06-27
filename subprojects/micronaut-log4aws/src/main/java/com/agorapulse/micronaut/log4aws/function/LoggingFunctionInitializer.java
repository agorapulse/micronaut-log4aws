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

import io.micronaut.context.ApplicationContext;
import io.micronaut.function.executor.FunctionInitializer;

public class LoggingFunctionInitializer extends FunctionInitializer {

    public LoggingFunctionInitializer() {
        // for AWS startup
        super();
    }

    public LoggingFunctionInitializer(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    public LoggingFunctionInitializer(ApplicationContext applicationContext, boolean inject) {
        super(applicationContext, inject);
    }

    @Override
    protected void startThis(ApplicationContext applicationContext) {
        Logging.runAndRethrow(getClass(), "Exception starting the application context", () -> super.startThis(applicationContext));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected ApplicationContext buildApplicationContext(Object context) {
        return Logging.callAndRethrow(getClass(), "Exception building the application context", () -> super.buildApplicationContext(context));
    }

    @Override
    protected void injectThis(ApplicationContext applicationContext) {
        Logging.runAndRethrow(getClass(), "Exception injecting the function handler", () -> super.injectThis(applicationContext));
    }

}
