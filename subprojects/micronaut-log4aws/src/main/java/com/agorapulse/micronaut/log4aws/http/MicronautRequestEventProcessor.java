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
package com.agorapulse.micronaut.log4aws.http;

import io.micronaut.http.HttpRequest;
import io.sentry.EventProcessor;
import io.sentry.SentryEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MicronautRequestEventProcessor implements EventProcessor {

    private static final List<String> SENSITIVE_HEADERS = Arrays.asList("X-FORWARDED-FOR", "AUTHORIZATION", "COOKIE");

    private static final List<String> SENSITIVE_PARAMS = Collections.singletonList("TOKEN");

    private final HttpRequest<?> request;

    public MicronautRequestEventProcessor(HttpRequest<?> request) {
        this.request = request;
    }

    @Override
    public SentryEvent process(SentryEvent event, Object hint) {
        event.setTag("req.path", request.getPath());
        event.setTag("req.method", request.getMethod().toString());

        // it can be actually null on read timeout
        if (request.getRemoteAddress() != null) {
            event.setTag("req.remoteHost", request.getRemoteAddress().getHostString());
        }

        event.setTag("req.serverHost", request.getServerAddress().getHostString());
        event.setExtra("req.parameters", resolveParameters(request));
        event.setExtra("req.headers", resolveHeaders(request));
        return event;
    }

    private static Map<String, String> resolveParameters(final HttpRequest<?> request) {
        return StreamSupport.stream(request.getParameters().spliterator(), false)
            .filter(e -> !SENSITIVE_PARAMS.contains(e.getKey().toUpperCase()))
            .collect(Collectors.toMap(Map.Entry::getKey, e -> String.join(",", e.getValue())));
    }

    private Map<String, String> resolveHeaders(final HttpRequest<?> request) {
        final Map<String, String> headersMap = new HashMap<>();
        for (String headerName : request.getHeaders().names()) {
            if (!SENSITIVE_HEADERS.contains(headerName.toUpperCase())) {
                headersMap.put(headerName, String.join(",", request.getHeaders().getAll(headerName)));
            }
        }
        return headersMap;
    }

    @Override
    public String toString() {
        return String.format("MicronautRequestBuildHelper{ request = %s }", request);
    }
}
