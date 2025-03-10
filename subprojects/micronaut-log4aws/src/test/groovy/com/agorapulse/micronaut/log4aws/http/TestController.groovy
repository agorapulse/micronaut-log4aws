/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2020-2025 Agorapulse.
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
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.validation.Validated

import jakarta.validation.constraints.NotBlank

@Validated
@CompileStatic
@Controller('/test')
class TestController {

    @Get('/{someparam}')
    HttpResponse<String> someparam(@PathVariable String someparam) {
        return HttpResponse.ok(someparam)
    }

    @Post('/{someerror}')
    @SuppressWarnings('ThrowRuntimeException')
    HttpResponse<String> someerror(@PathVariable String someerror) {
        throw new RuntimeException(someerror)
    }

    @Put('/validated')
    HttpResponse<String> validationIssue(@NotBlank String text) {
        return HttpResponse.ok(text)
    }

}
