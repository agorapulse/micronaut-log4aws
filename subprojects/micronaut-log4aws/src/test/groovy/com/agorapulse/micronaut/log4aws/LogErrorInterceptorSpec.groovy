package com.agorapulse.micronaut.log4aws

import io.micronaut.context.ApplicationContext
import org.junit.Rule
import org.junit.contrib.java.lang.system.SystemOutRule
import org.slf4j.LoggerFactory
import spock.lang.AutoCleanup
import spock.lang.Specification

import javax.inject.Singleton
class LogErrorInterceptorSpec extends Specification {

    private static final String LOGGED_EVENT = 'ERROR ThrowsIllegalArgumentException:27 - Exception executing method'

    @Rule SystemOutRule systemOutRule = new SystemOutRule().enableLog()

    @AutoCleanup ApplicationContext context = ApplicationContext.run()

    void setup() {
        LoggerFactory

    }

    void 'test exception logged'() {
        given:
            ThrowsIllegalArgumentException tester = context.getBean(ThrowsIllegalArgumentException)
        when:
            tester.normal()
        then:
            thrown IllegalArgumentException

            systemOutRule.log.contains(LOGGED_EVENT)
    }

    void 'test exception muted'() {
        given:
            ThrowsIllegalArgumentException tester = context.getBean(ThrowsIllegalArgumentException)
        when:
            tester.muted()
        then:
            noExceptionThrown()

            systemOutRule.log.contains(LOGGED_EVENT)
    }

    void 'test exception filtered'() {
        given:
            ThrowsIllegalArgumentException tester = context.getBean(ThrowsIllegalArgumentException)
        when:
            tester.filtered()
        then:
            thrown IllegalArgumentException

            !systemOutRule.log.contains(LOGGED_EVENT)
    }

    void 'test exception filtered and muted match'() {
        given:
            ThrowsIllegalArgumentException tester = context.getBean(ThrowsIllegalArgumentException)
        when:
            tester.filteredAndMutedMatch()
        then:
            noExceptionThrown()

            systemOutRule.log.contains(LOGGED_EVENT)
    }

    void 'test exception filtered and muted mismatch'() {
        given:
            ThrowsIllegalArgumentException tester = context.getBean(ThrowsIllegalArgumentException)
        when:
            tester.filteredAndMutedMismatch()
        then:
            thrown IllegalArgumentException

            !systemOutRule.log.contains(LOGGED_EVENT)
    }

}

@Singleton
class ThrowsIllegalArgumentException {

    @LogError
    void normal() {
        throw new IllegalArgumentException()
    }

    @LogError(mute = true)
    void muted() {
        throw new IllegalArgumentException()
    }

    @LogError(only = IllegalStateException)
    void filtered() {
        throw new IllegalArgumentException()
    }

    @LogError(mute = true, only = IllegalArgumentException)
    void filteredAndMutedMatch() {
        throw new IllegalArgumentException()
    }

    @LogError(mute = true, only = IllegalStateException)
    void filteredAndMutedMismatch() {
        throw new IllegalArgumentException()
    }

}
