package com.agorapulse.micronaut.log4aws.http

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Error
import io.micronaut.http.hateoas.JsonError
import io.micronaut.web.router.exceptions.UnsatisfiedRouteException

@Controller
@Slf4j
@CompileStatic
class GlobalErrorHandler {

    // Default handler, will not be used if the exception is handled by another specific one
    @Error(global = true, exception = Exception)
    HttpResponse<JsonError> serverError(Exception e) {
        log.error(e.message, e)
        if (e instanceof UnsatisfiedRouteException) {
            return HttpResponse.badRequest(new JsonError(e.message))
        }
        return HttpResponse.<JsonError>serverError().body(new JsonError(e.message))
    }

}
