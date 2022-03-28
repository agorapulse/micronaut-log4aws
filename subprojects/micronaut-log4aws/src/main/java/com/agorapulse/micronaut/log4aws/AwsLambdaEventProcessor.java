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
package com.agorapulse.micronaut.log4aws;

import io.micronaut.context.annotation.Context;
import io.sentry.EventProcessor;
import io.sentry.SentryEvent;

@Context
public class AwsLambdaEventProcessor implements EventProcessor {

    @Override
    public SentryEvent process(SentryEvent event, Object hint) {
        event.setTag("aws_region", System.getenv("AWS_REGION"));
        event.setTag("aws_default_region", System.getenv("AWS_DEFAULT_REGION"));
        event.setTag("lambda_function_name", System.getenv("AWS_LAMBDA_FUNCTION_NAME"));
        event.setTag("lambda_function_version", System.getenv("AWS_LAMBDA_FUNCTION_VERSION"));
        event.setTag("lambda_handler", System.getenv("_HANDLER"));
        event.setTag("lambda_execution_environment", System.getenv("AWS_EXECUTION_ENV"));
        event.setTag("lambda_log_group_name", System.getenv("AWS_LAMBDA_LOG_GROUP_NAME"));
        event.setTag("lambda_log_stream_name", System.getenv("AWS_LAMBDA_LOG_STREAM_NAME"));
        event.setTag("lambda_memory_size", System.getenv("AWS_LAMBDA_FUNCTION_MEMORY_SIZE"));
        return event;
    }

}
