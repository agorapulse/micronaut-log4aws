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
