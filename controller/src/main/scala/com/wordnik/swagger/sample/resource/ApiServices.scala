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

package com.wordnik.swagger.sample.resource

import com.wordnik.swagger.sample.service._
import com.wordnik.swagger.sample.model._
import com.wordnik.swagger.annotations._
import com.wordnik.swagger.core._
import com.wordnik.swagger.jaxrs._
import com.wordnik.swagger.sample.exception.NotFoundException

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.core.Response
import javax.ws.rs.core.Context
import javax.ws.rs._

import scala.collection.JavaConverters._

@Path("/services")
@Api(value = "/services", description = "All services for the cluster")
@Produces(Array("application/json;charset=utf8"))
class ApiServices {
  @GET
  @ApiOperation(value = "gets registered nodes",
    response = classOf[String],
    responseContainer = "List")
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "TODO"))
  )
  def registeredNodes(
    @ApiParam(value = "Authorization Code", required = true)@HeaderParam("access_token") accessToken: String) = {
    val data = RegistrationService.services.asJava
    Response.ok.entity(data).build
  }
}
