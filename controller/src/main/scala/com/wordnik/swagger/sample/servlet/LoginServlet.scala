/**
 *  Copyright 2013 Wordnik, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.wordnik.swagger.sample.servlet

import com.wordnik.swagger.annotations._
import com.wordnik.swagger.sample.model._
import com.wordnik.swagger.sample.service.AuthService

import com.wordnik.swagger.core.util.JsonSerializer

import java.io.IOException
import java.util.Date

import javax.servlet.ServletException
import javax.servlet.http.{ HttpServlet, HttpServletRequest, HttpServletResponse }

// @Api(value = "/oauth/login", description = "requestToken")
class LoginServlet extends HttpServlet {
  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  @ApiOperation(httpMethod = "POST", value = "Resource to request a token, given client_id and client_secret", consumes = "application/x-www-form-urlencoded")
  @ApiResponses(Array(new ApiResponse(code = 405, message = "Invalid input")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "accept", value = "Client accepts login request", required = false, dataType = "string", paramType = "form"),
    new ApiImplicitParam(name = "scope", value = "Scope for access", required = false, dataType = "string", paramType = "form"),
    new ApiImplicitParam(name = "client_id", value = "Client identifier", required = true, dataType = "string", paramType = "form"),
    new ApiImplicitParam(name = "username", value = "Username", required = true, dataType = "string", paramType = "form"),
    new ApiImplicitParam(name = "password", value = "Password", required = true, dataType = "string", paramType = "form"),
    new ApiImplicitParam(name = "redirect_uri", value = "Redirection URI", required = true, dataType = "string", paramType = "form")))
  override protected def doPost(request: HttpServletRequest, response: HttpServletResponse) = {
    new AuthService().login(request, response)
  }

  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  override protected def doGet(request: HttpServletRequest, response: HttpServletResponse) = {
    response.setStatus(400)
    response.setContentType("application/json")
    response.getOutputStream.write(JsonSerializer.asJson(ApiResponseMessage(400, "get requests are not supported")).getBytes("utf-8"))
  }
}
