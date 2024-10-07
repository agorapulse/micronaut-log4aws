/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2024 Agorapulse.
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

import io.sentry.*;
import io.sentry.exception.InvalidSentryTraceHeaderException;
import io.sentry.protocol.SentryId;
import io.sentry.protocol.SentryTransaction;
import io.sentry.protocol.User;

import java.util.*;

public class MockHub implements IHub {

    private final List<Scope> scopes = new ArrayList<>();
    private int pointer = -1;

    public List<Scope> getScopes() {
        return scopes;
    }

    public int getPointer() {
        return pointer;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void captureUserFeedback(UserFeedback userFeedback) {
        System.err.printf("Got a feedback %s when scope is %s%n", userFeedback, getCurrentScope());
    }

    @Override
    public void startSession() {
        System.err.printf("Starting the new session");
    }

    @Override
    public void endSession() {
        System.err.printf("Ending the current session");
    }

    @Override
    public void close() {
        System.err.printf("Closing the hub");
    }

    @Override
    public void addBreadcrumb(Breadcrumb breadcrumb, Hint hint) {
        System.err.printf("Got breadcrumb %s with hint %s when scope is %s%n", breadcrumb, hint, getCurrentScope());
        getCurrentScope().addBreadcrumb(breadcrumb, hint);
    }

    @Override
    public void setLevel(SentryLevel level) {
        System.err.printf("New level is " + level);
        getCurrentScope().setLevel(level);
    }

    @Override
    public void setTransaction(String transaction) {
        System.err.printf("New transaction is " + transaction);
        getCurrentScope().setTransaction(transaction);
    }

    @Override
    public void setUser(User user) {
        System.err.printf("New user is " + user);
        getCurrentScope().setUser(user);
    }

    @Override
    public void setFingerprint(List<String> fingerprint) {
        System.err.printf("New fingerprint is " + fingerprint);
        getCurrentScope().setFingerprint(fingerprint);
    }

    @Override
    public void clearBreadcrumbs() {
        System.err.printf("Clearing breadcrumbs");
        getCurrentScope().clearBreadcrumbs();
    }

    @Override
    public void setTag(String key, String value) {
        System.err.printf("Setting tag with key %s and value %s when scope is %s%n", key, value, getCurrentScope());
        getCurrentScope().setTag(key, value);
    }

    @Override
    public void removeTag(String key) {
        System.err.printf("Removing tag with key %s when scope is %s%n", key, getCurrentScope());
        getCurrentScope().removeTag(key);
    }

    @Override
    public void setExtra(String key, String value) {
        System.err.printf("Setting extra with key %s and value %s when scope is %s%n", key, value, getCurrentScope());
        getCurrentScope().setExtra(key, value);
    }

    @Override
    public void removeExtra(String key) {
        System.err.printf("Removing extra with key %s when scope is %s%n", key, getCurrentScope());
        getCurrentScope().removeExtra(key);
    }

    @Override
    public SentryId getLastEventId() {
        return new SentryId();
    }

    @Override
    public void pushScope() {
        pointer++;
        scopes.add(pointer, new Scope(getOptions()));
        System.err.printf("Pushed scope %s%n", getCurrentScope());
    }

    @Override
    public void popScope() {
        System.err.printf("Popping scope %s%n", getCurrentScope());
        pointer--;
    }

    @Override
    public void withScope(ScopeCallback callback) {
        pushScope();
        configureScope(callback);
        popScope();
    }

    @Override
    public void configureScope(ScopeCallback callback) {
        System.err.printf("Configuring scope %s%n", getCurrentScope());
        callback.run(getCurrentScope());
    }

    @Override
    public void bindClient(ISentryClient client) {
        System.err.printf("Binding client %s when scope is %s%n", client, getCurrentScope());
    }

    @Override
    public void flush(long timeoutMillis) {
        System.err.printf("Flushing with timeout %s when scope is %s%n", timeoutMillis, getCurrentScope());
    }


    //CHECKSTYLE:OFF
    @Override
    public IHub clone() {
        return this;
    }

    @Override
    public SentryId captureTransaction(SentryTransaction transaction, TraceContext traceContext, Hint hint, ProfilingTraceData profilingTraceData) {
        System.err.printf("Capturing transaction %s with state %s and hint %s when scope is %s%n", transaction, profilingTraceData, hint, getCurrentScope());
        return new SentryId();
    }
    //CHECKSTYLE:ON

    @Override
    public ITransaction startTransaction(TransactionContext transactionContexts, CustomSamplingContext customSamplingContext, boolean bindToScope) {
        System.err.printf("Capturing transaction with contexts %s with sampling context %s and bind to scope %s when scope is %s%n", transactionContexts, customSamplingContext, bindToScope, getCurrentScope());
        return getCurrentScope().getTransaction();
    }

    @Override
    public ITransaction startTransaction(TransactionContext transactionContext, TransactionOptions transactionOptions) {
        //System.err.printf("Capturing transaction with contexts %s with sampling context %s and bind to scope %s and start timestamp %s when scope is %s%n", transactionContext, transactionOptions, getCurrentScope());
        return getCurrentScope().getTransaction();
    }

    @Override
    public SentryTraceHeader traceHeaders() {
        try {
            return new SentryTraceHeader("traceid-spanid");
        } catch (InvalidSentryTraceHeaderException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setSpanContext(Throwable throwable, ISpan span, String transactionName) {
        System.err.printf("Setting span context %s to span %s with transaction name %s when scope is %s%n", throwable, span, transactionName, getCurrentScope());
    }

    @Override
    public ISpan getSpan() {
        return NoOpSpan.getInstance();
    }

    @Override
    public SentryOptions getOptions() {
        return new SentryOptions();
    }

    @Override
    public Boolean isCrashedLastRun() {
        return false;
    }

    @Override
    public void reportFullyDisplayed() {

    }

    private Scope getCurrentScope() {
        if (pointer < 0) {
            return null;
        }
        return scopes.get(pointer);
    }

    @Override
    public SentryId captureEvent(SentryEvent event, Hint hint) {
        System.err.printf("Got an event %s with hint %s when scope is %s%n", event, hint, getCurrentScope());
        return new SentryId();
    }

    @Override
    public SentryId captureEvent(SentryEvent event, Hint hint, ScopeCallback callback) {
        return null;
    }

    @Override
    public SentryId captureMessage(String message, SentryLevel level) {
        System.err.printf("Got a message %s with level %s when scope is %s%n", message, level, getCurrentScope());
        return new SentryId();
    }

    @Override
    public SentryId captureMessage(String message, SentryLevel level, ScopeCallback callback) {
        return null;
    }

    @Override
    public SentryId captureEnvelope(SentryEnvelope envelope, Hint hint) {
        System.err.printf("Got an envelope %s with hint %s when scope is %s%n", envelope, hint, getCurrentScope());
        return new SentryId();
    }

    @Override
    public SentryId captureException(Throwable throwable, Hint hint) {
        System.err.printf("Got an envelope %s with hint %s when scope is %s%n", throwable, hint, getCurrentScope());
        return new SentryId();
    }

    @Override
    public SentryId captureException(Throwable throwable, Hint hint, ScopeCallback callback) {
        return null;
    }



}
