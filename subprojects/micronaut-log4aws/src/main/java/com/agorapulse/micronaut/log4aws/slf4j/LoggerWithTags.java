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
package com.agorapulse.micronaut.log4aws.slf4j;

import org.slf4j.Logger;
import org.slf4j.Marker;

import java.util.Map;

public class LoggerWithTags implements Logger {
    private final Logger logger;
    private final Map<String, String> tags;

    public LoggerWithTags(Logger logger, Map<String, String> tags) {
        this.logger = logger;
        this.tags = tags;
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        TagHelper.setTags(tags);
        logger.trace(msg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void trace(String format, Object arg) {
        TagHelper.setTags(tags);
        logger.trace(format, arg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        TagHelper.setTags(tags);
        logger.trace(format, arg1, arg2);
        TagHelper.clearTags(tags);
    }

    @Override
    public void trace(String format, Object... arguments) {
        TagHelper.setTags(tags);
        logger.trace(format, arguments);
        TagHelper.clearTags(tags);
    }

    @Override
    public void trace(String msg, Throwable t) {
        TagHelper.setTags(tags);
        logger.trace(msg, t);
        TagHelper.clearTags(tags);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return logger.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        TagHelper.setTags(tags);
        logger.trace(marker, msg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        TagHelper.setTags(tags);
        logger.trace(marker, format, arg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        TagHelper.setTags(tags);
        logger.trace(marker, format, arg1, arg2);
        TagHelper.clearTags(tags);
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        TagHelper.setTags(tags);
        logger.trace(marker, format, argArray);
        TagHelper.clearTags(tags);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        TagHelper.setTags(tags);
        logger.trace(marker, msg, t);
        TagHelper.clearTags(tags);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        TagHelper.setTags(tags);
        logger.debug(msg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void debug(String format, Object arg) {
        TagHelper.setTags(tags);
        logger.debug(format, arg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        TagHelper.setTags(tags);
        logger.debug(format, arg1, arg2);
        TagHelper.clearTags(tags);
    }

    @Override
    public void debug(String format, Object... arguments) {
        TagHelper.setTags(tags);
        logger.debug(format, arguments);
        TagHelper.clearTags(tags);
    }

    @Override
    public void debug(String msg, Throwable t) {
        TagHelper.setTags(tags);
        logger.debug(msg, t);
        TagHelper.clearTags(tags);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg) {
        TagHelper.setTags(tags);
        logger.debug(marker, msg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        TagHelper.setTags(tags);
        logger.debug(marker, format, arg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        TagHelper.setTags(tags);
        logger.debug(marker, format, arg1, arg2);
        TagHelper.clearTags(tags);
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        TagHelper.setTags(tags);
        logger.debug(marker, format, arguments);
        TagHelper.clearTags(tags);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        TagHelper.setTags(tags);
        logger.debug(marker, msg, t);
        TagHelper.clearTags(tags);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        TagHelper.setTags(tags);
        logger.info(msg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void info(String format, Object arg) {
        TagHelper.setTags(tags);
        logger.info(format, arg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        TagHelper.setTags(tags);
        logger.info(format, arg1, arg2);
        TagHelper.clearTags(tags);
    }

    @Override
    public void info(String format, Object... arguments) {
        TagHelper.setTags(tags);
        logger.info(format, arguments);
        TagHelper.clearTags(tags);
    }

    @Override
    public void info(String msg, Throwable t) {
        TagHelper.setTags(tags);
        logger.info(msg, t);
        TagHelper.clearTags(tags);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        TagHelper.setTags(tags);
        logger.info(marker, msg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        TagHelper.setTags(tags);
        logger.info(marker, format, arg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        TagHelper.setTags(tags);
        logger.info(marker, format, arg1, arg2);
        TagHelper.clearTags(tags);
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        TagHelper.setTags(tags);
        logger.info(marker, format, arguments);
        TagHelper.clearTags(tags);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        TagHelper.setTags(tags);
        logger.info(marker, msg, t);
        TagHelper.clearTags(tags);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        TagHelper.setTags(tags);
        logger.warn(msg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void warn(String format, Object arg) {
        TagHelper.setTags(tags);
        logger.warn(format, arg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void warn(String format, Object... arguments) {
        TagHelper.setTags(tags);
        logger.warn(format, arguments);
        TagHelper.clearTags(tags);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        TagHelper.setTags(tags);
        logger.warn(format, arg1, arg2);
        TagHelper.clearTags(tags);
    }

    @Override
    public void warn(String msg, Throwable t) {
        TagHelper.setTags(tags);
        logger.warn(msg, t);
        TagHelper.clearTags(tags);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return logger.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String msg) {
        TagHelper.setTags(tags);
        logger.warn(marker, msg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        TagHelper.setTags(tags);
        logger.warn(marker, format, arg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        TagHelper.setTags(tags);
        logger.warn(marker, format, arg1, arg2);
        TagHelper.clearTags(tags);
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        TagHelper.setTags(tags);
        logger.warn(marker, format, arguments);
        TagHelper.clearTags(tags);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        TagHelper.setTags(tags);
        logger.warn(marker, msg, t);
        TagHelper.clearTags(tags);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        TagHelper.setTags(tags);
        logger.error(msg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void error(String format, Object arg) {
        TagHelper.setTags(tags);
        logger.error(format, arg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        TagHelper.setTags(tags);
        logger.error(format, arg1, arg2);
        TagHelper.clearTags(tags);
    }

    @Override
    public void error(String format, Object... arguments) {
        TagHelper.setTags(tags);
        logger.error(format, arguments);
        TagHelper.clearTags(tags);
    }

    @Override
    public void error(String msg, Throwable t) {
        TagHelper.setTags(tags);
        logger.error(msg, t);
        TagHelper.clearTags(tags);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return logger.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg) {
        TagHelper.setTags(tags);
        logger.error(marker, msg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        TagHelper.setTags(tags);
        logger.error(marker, format, arg);
        TagHelper.clearTags(tags);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        TagHelper.setTags(tags);
        logger.error(marker, format, arg1, arg2);
        TagHelper.clearTags(tags);
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        TagHelper.setTags(tags);
        logger.error(marker, format, arguments);
        TagHelper.clearTags(tags);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        TagHelper.setTags(tags);
        logger.error(marker, msg, t);
        TagHelper.clearTags(tags);
    }

}
