package com.agorapulse.micronaut.log4aws.demo

import io.micronaut.context.ApplicationContext
import spock.lang.AutoCleanup
import spock.lang.Specification

class LoggingServiceSpec extends Specification {

    @AutoCleanup ApplicationContext context = ApplicationContext.build().build().start()

    LoggingService service = context.getBean(LoggingService)

    void 'exception thrown'() {
        when:
            service.apply(
                trace: 'Testing Trace',
                debug: 'Testing Debug',
                warn: 'Testing Warning',
                error: 'Testing Error',
                throw: 'Testing Exception'
            )
        then:
            thrown(RuntimeException)
    }

}
