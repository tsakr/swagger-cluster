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
import com.wordnik.swagger.sample.model.ApiResponseMessage
import com.wordnik.swagger.sample.service.AuthService

import com.wordnik.swagger.core.util.JsonSerializer

import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.{ HttpServlet, HttpServletRequest, HttpServletResponse }

// @Api(value = "/oauth/tokenStatus", description = "provides an oauth token status")
class TokenStatus extends HttpServlet {
  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  @ApiOperation(httpMethod = "GET", value = "returns an api status message", response = classOf[ApiResponseMessage])
  @ApiResponses(Array(new ApiResponse(code = 400, message = "Invalid input")))
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "access_token", value = "token to verify", paramType = "query")))
  override protected def doGet(request: HttpServletRequest, response: HttpServletResponse) = {
    try {
    val accessCode = request.getCookies().filter(_.getName == "access_token") match {
      case e: Array[_] if(e.size > 0) => e.head.getValue
      case _ => request.getParameter("access_token")
    }
    val apiResponse = new AuthService().tokenStatus(accessCode)
    response.setContentType("application/json")
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
    response.setDateHeader("Expires", 1);
    response.getOutputStream.write(JsonSerializer.asJson(apiResponse).getBytes("utf-8"))}
    catch {
      case e: Exception => e.printStackTrace
    }
  }

  @throws(classOf[IOException])
  @throws(classOf[ServletException])
  override protected def doPost(request: HttpServletRequest, response: HttpServletResponse) = {
    doGet(request, response)
  }
}