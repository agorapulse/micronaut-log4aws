/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2021 Agorapulse.
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

import io.sentry.event.EventBuilder;
import io.sentry.event.helper.EventBuilderHelper;

public class AwsLambdaEventBuildHelper implements EventBuilderHelper {

    @Override
    public void helpBuildingEvent(EventBuilder eventBuilder) {
        eventBuilder.withTag("aws_region", System.getenv("AWS_REGION"));
        eventBuilder.withTag("aws_default_region", System.getenv("AWS_DEFAULT_REGION"));
        eventBuilder.withTag("lambda_function_name", System.getenv("AWS_LAMBDA_FUNCTION_NAME"));
        eventBuilder.withTag("lambda_function_version", System.getenv("AWS_LAMBDA_FUNCTION_VERSION"));
        eventBuilder.withTag("lambda_handler", System.getenv("_HANDLER"));
        eventBuilder.withTag("lambda_execution_environment", System.getenv("AWS_EXECUTION_ENV"));
        eventBuilder.withTag("lambda_log_group_name", System.getenv("AWS_LAMBDA_LOG_GROUP_NAME"));
        eventBuilder.withTag("lambda_log_stream_name", System.getenv("AWS_LAMBDA_LOG_STREAM_NAME"));
        eventBuilder.withTag("lambda_memory_size", System.getenv("AWS_LAMBDA_FUNCTION_MEMORY_SIZE"));
    }

}
