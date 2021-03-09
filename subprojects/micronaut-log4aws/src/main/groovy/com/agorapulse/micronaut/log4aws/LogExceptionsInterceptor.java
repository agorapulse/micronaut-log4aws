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

import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import io.micronaut.core.annotation.AnnotationValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class LogExceptionsInterceptor implements MethodInterceptor<Object, Object> {

    @Override
    public Object intercept(MethodInvocationContext<Object, Object> context) {
        Optional<AnnotationValue<LogError>> annotation = Optional.ofNullable(context.getAnnotation(LogError.class));

        boolean mute = annotation.flatMap(a -> a.booleanValue("mute")).orElse(false);
        Class<?> filter = annotation.flatMap(a -> a.classValue("only")).orElse(Throwable.class);

        try {
            return context.proceed();
        } catch (Throwable th) {
            if (filter.isAssignableFrom(th.getClass())) {
                Logger logger = LoggerFactory.getLogger(context.getTargetMethod().getDeclaringClass());
                logger.error("Exception executing method " + context.getTargetMethod(), th);
                if (mute) {
                    return null;
                }
            }
            throw th;
        }
    }
}
