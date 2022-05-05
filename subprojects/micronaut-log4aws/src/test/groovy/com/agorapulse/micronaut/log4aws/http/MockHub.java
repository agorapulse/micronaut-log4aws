package com.agorapulse.micronaut.log4aws.http;

import io.sentry.*;
import io.sentry.exception.InvalidSentryTraceHeaderException;
import io.sentry.protocol.SentryId;
import io.sentry.protocol.SentryTransaction;
import io.sentry.protocol.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public @NotNull SentryId captureEvent(@NotNull SentryEvent event, @Nullable Object hint) {
        System.err.printf("Got an event %s with hint %s when scope is %s%n", event, hint, getCurrentScope());
        return new SentryId();
    }

    @Override
    public @NotNull SentryId captureMessage(@NotNull String message, @NotNull SentryLevel level) {
        System.err.printf("Got a message %s with level %s when scope is %s%n", message, level, getCurrentScope());
        return new SentryId();
    }

    @Override
    public @NotNull SentryId captureEnvelope(@NotNull SentryEnvelope envelope, @Nullable Object hint) {
        System.err.printf("Got an envelope %s with hint %s when scope is %s%n", envelope, hint, getCurrentScope());
        return new SentryId();
    }

    @Override
    public @NotNull SentryId captureException(@NotNull Throwable throwable, @Nullable Object hint) {
        System.err.printf("Got an envelope %s with hint %s when scope is %s%n", throwable, hint, getCurrentScope());
        return new SentryId();
    }

    @Override
    public void captureUserFeedback(@NotNull UserFeedback userFeedback) {
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
    public void addBreadcrumb(@NotNull Breadcrumb breadcrumb, @Nullable Object hint) {
        System.err.printf("Got breadcrumb %s with hint %s when scope is %s%n", breadcrumb, hint, getCurrentScope());
        getCurrentScope().addBreadcrumb(breadcrumb, hint);
    }

    @Override
    public void setLevel(@Nullable SentryLevel level) {
        System.err.printf("New level is " + level);
        getCurrentScope().setLevel(level);
    }

    @Override
    public void setTransaction(@Nullable String transaction) {
        System.err.printf("New transaction is " + transaction);
        getCurrentScope().setTransaction(transaction);
    }

    @Override
    public void setUser(@Nullable User user) {
        System.err.printf("New user is " + user);
        getCurrentScope().setUser(user);
    }

    @Override
    public void setFingerprint(@NotNull List<String> fingerprint) {
        System.err.printf("New fingerprint is " + fingerprint);
        getCurrentScope().setFingerprint(fingerprint);
    }

    @Override
    public void clearBreadcrumbs() {
        System.err.printf("Clearing breadcrumbs");
        getCurrentScope().clearBreadcrumbs();
    }

    @Override
    public void setTag(@NotNull String key, @NotNull String value) {
        System.err.printf("Setting tag with key %s and value %s when scope is %s%n", key, value, getCurrentScope());
        getCurrentScope().setTag(key, value);
    }

    @Override
    public void removeTag(@NotNull String key) {
        System.err.printf("Removing tag with key %s when scope is %s%n", key, getCurrentScope());
        getCurrentScope().removeTag(key);
    }

    @Override
    public void setExtra(@NotNull String key, @NotNull String value) {
        System.err.printf("Setting extra with key %s and value %s when scope is %s%n", key, value, getCurrentScope());
        getCurrentScope().setExtra(key, value);
    }

    @Override
    public void removeExtra(@NotNull String key) {
        System.err.printf("Removing extra with key %s when scope is %s%n", key, getCurrentScope());
        getCurrentScope().removeExtra(key);
    }

    @Override
    public @NotNull SentryId getLastEventId() {
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
    public void withScope(@NotNull ScopeCallback callback) {
        pushScope();
        configureScope(callback);
        popScope();
    }

    @Override
    public void configureScope(@NotNull ScopeCallback callback) {
        System.err.printf("Configuring scope %s%n", getCurrentScope());
        callback.run(getCurrentScope());
    }

    @Override
    public void bindClient(@NotNull ISentryClient client) {
        System.err.printf("Binding client %s when scope is %s%n", client, getCurrentScope());
    }

    @Override
    public void flush(long timeoutMillis) {
        System.err.printf("Flushing with timeout %s when scope is %s%n", timeoutMillis, getCurrentScope());
    }


    //CHECKSTYLE:OFF
    @Override
    public @NotNull IHub clone() {
        return this;
    }
    //CHECKSTYLE:ON

    @Override
    public @NotNull SentryId captureTransaction(@NotNull SentryTransaction transaction, @Nullable TraceState traceState, @Nullable Object hint) {
        System.err.printf("Capturing transaction %s with state %s and hint %s when scope is %s%n", transaction, traceState, hint, getCurrentScope());
        return new SentryId();
    }

    @Override
    public @NotNull ITransaction startTransaction(@NotNull TransactionContext transactionContexts, @Nullable CustomSamplingContext customSamplingContext, boolean bindToScope) {
        System.err.printf("Capturing transaction with contexts %s with sampling context %s and bind to scope %s when scope is %s%n", transactionContexts, customSamplingContext, bindToScope, getCurrentScope());
        return getCurrentScope().getTransaction();
    }

    @Override
    public @NotNull ITransaction startTransaction(@NotNull TransactionContext transactionContexts, @Nullable CustomSamplingContext customSamplingContext, boolean bindToScope, @Nullable Date startTimestamp) {
        System.err.printf("Capturing transaction with contexts %s with sampling context %s and bind to scope %s and start timestamp %s when scope is %s%n", transactionContexts, customSamplingContext, bindToScope, startTimestamp, getCurrentScope());
        return getCurrentScope().getTransaction();
    }

    @Override
    public @NotNull ITransaction startTransaction(@NotNull TransactionContext transactionContexts, @Nullable CustomSamplingContext customSamplingContext, boolean bindToScope, @Nullable Date startTimestamp, boolean waitForChildren, @Nullable TransactionFinishedCallback transactionFinishedCallback) {
        System.err.printf("Capturing transaction with contexts %s with sampling context %s and bind to scope %s and start timestamp %s and wait for children %s and callback %s when scope is %s%n", transactionContexts, customSamplingContext, bindToScope, startTimestamp, waitForChildren, transactionContexts, getCurrentScope());
        return getCurrentScope().getTransaction();
    }

    @Override
    public @Nullable SentryTraceHeader traceHeaders() {
        try {
            return new SentryTraceHeader("traceid-spanid");
        } catch (InvalidSentryTraceHeaderException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setSpanContext(@NotNull Throwable throwable, @NotNull ISpan span, @NotNull String transactionName) {
        System.err.printf("Setting span context %s to span %s with transaction name %s when scope is %s%n", throwable, span, transactionName, getCurrentScope());
    }

    @Override
    public @Nullable ISpan getSpan() {
        return NoOpSpan.getInstance();
    }

    @Override
    public @NotNull SentryOptions getOptions() {
        return new SentryOptions();
    }

    @Override
    public @Nullable Boolean isCrashedLastRun() {
        return false;
    }

    private Scope getCurrentScope() {
        if (pointer < 0) {
            return null;
        }
        return scopes.get(pointer);
    }
}
