/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020 Agorapulse.
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
package com.agorapulse.micronaut.log4aws.http;

import io.micronaut.http.HttpRequest;
import io.sentry.EventProcessor;
import io.sentry.SentryEvent;
import io.sentry.protocol.Request;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MicronautHttpRequestEventProcessor implements EventProcessor {

    private static final List<String> SENSITIVE_HEADERS =
        Arrays.asList("X-FORWARDED-FOR", "AUTHORIZATION", "COOKIE");

    private static final List<String> SENSITIVE_PARAMS =
        Arrays.asList("TOKEN");

    private final HttpRequest<?> httpRequest;

    public MicronautHttpRequestEventProcessor(HttpRequest<?> httpRequest) {
        this.httpRequest = httpRequest;
    }

    // httpRequest.getRequestURL() returns StringBuffer which is considered an obsolete class.
    @SuppressWarnings("JdkObsolete")
    @Override
    public SentryEvent process(SentryEvent event, Object hint) {
        final Request sentryRequest = new Request();

        sentryRequest.setMethod(httpRequest.getMethod().toString());
        sentryRequest.setQueryString(resolveParameters());
        sentryRequest.setUrl(httpRequest.getPath());
        sentryRequest.setHeaders(resolveHeadersMap(httpRequest));

        event.setRequest(sentryRequest);
        return event;
    }

    private String resolveParameters() {
        return StreamSupport.stream(httpRequest.getParameters().spliterator(), false)
            .filter(e -> !SENSITIVE_PARAMS.contains(e.getKey().toUpperCase()))
            .map(e -> e.getKey() + "=" + String.join(",", e.getValue()))
            .collect(Collectors.joining("&"));
    }

    private Map<String, String> resolveHeadersMap(final HttpRequest<?> request) {
        final Map<String, String> headersMap = new HashMap<>();
        for (String headerName : request.getHeaders().names()) {
            // do not copy personal information identifiable headers
            if (!SENSITIVE_HEADERS.contains(headerName.toUpperCase())) {
                headersMap.put(headerName, String.join(",", request.getHeaders().getAll(headerName)));
            }
        }
        return headersMap;
    }

}
