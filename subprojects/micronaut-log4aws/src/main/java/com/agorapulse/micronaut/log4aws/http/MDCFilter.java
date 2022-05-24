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
package com.agorapulse.micronaut.log4aws.http;

import com.agorapulse.micronaut.log4aws.slf4j.TagHelper;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.order.Ordered;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Filter("/**")
@Requires(property = "sentry.filter.type", value = "mdc")
@Replaces(SentryFilter.class)
public class MDCFilter implements HttpServerFilter {

    private static final List<String> SENSITIVE_HEADERS = Arrays.asList("X-FORWARDED-FOR", "AUTHORIZATION", "COOKIE");

    private static final List<String> SENSITIVE_PARAMS = Collections.singletonList("TOKEN");

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1000;
    }

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        Map<String, String> tags = getTags(request);
        return Flowable
            .just(request)
            .switchMap(r -> {
                TagHelper.setTags(tags);
                return chain.proceed(request);
            })
            .doFinally(() -> TagHelper.clearTags(tags));
    }

    public Map<String, String> getTags(HttpRequest<?> request) {
        Map<String, String> tags = new LinkedHashMap<>();
        tags.put("req.path", request.getPath());
        tags.put("req.method", request.getMethod().toString());

        // it can be actually null on read timeout
        if (request.getRemoteAddress() != null) {
            tags.put("req.remoteHost", request.getRemoteAddress().getHostString());
        }

        tags.put("req.serverHost", request.getServerAddress().getHostString());
        tags.put("req.parameters", resolveParameters(request));
        tags.put("req.headers", resolveHeaders(request));
        return tags;
    }

    private static String resolveParameters(final HttpRequest<?> request) {
        return StreamSupport.stream(request.getParameters().spliterator(), false)
                .filter(e -> !SENSITIVE_PARAMS.contains(e.getKey().toUpperCase()))
                .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
                .collect(Collectors.joining("&"));
    }

    private String resolveHeaders(final HttpRequest<?> request) {
        return request.getHeaders().names().stream()
                .filter(headerName -> !SENSITIVE_HEADERS.contains(headerName.toUpperCase()))
                .map(headerName -> headerName + ": " + String.join(",", request.getHeaders().getAll(headerName)))
                .collect(Collectors.joining(","));
    }

}
