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
