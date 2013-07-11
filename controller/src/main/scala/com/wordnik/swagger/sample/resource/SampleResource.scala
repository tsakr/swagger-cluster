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

@Path("/register")
@Api(value = "/register", description = "Registers a node")
@Produces(Array("application/json;charset=utf8"))
class SampleResource {
  @POST
  @ApiOperation(value = "registers a node",
    response = classOf[ApiResponseMessage])
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "TODO"))
  )
  def registerNode(
    @ApiParam(value = "Authorization Code", required = true)@HeaderParam("access_token") accessToken: String,
    @ApiParam(value = "Node to register", required = true) node: ServiceNode) = {
    Response.ok.entity(RegistrationService.register(node)).build
  }

  @GET
  @Path("/clients")
  @ApiOperation(value = "clients in healthy state",
    response = classOf[ServiceNode],
    responseContainer = "List")
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "TODO"))
  )
  def clients(
    @ApiParam(value = "Authorization Code", required = true)@HeaderParam("access_token") accessToken: String) = {
    Response.ok.entity(RegistrationService.healthyClients()).build
  }

  @GET
  @Path("/clients/failing")
  @ApiOperation(value = "Clients in down state",
    response = classOf[ServiceNode],
    responseContainer = "List")
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "TODO"))
  )
  def failingClients (
    @ApiParam(value = "Authorization Code", required = true)@HeaderParam("access_token") accessToken: String) = {
    Response.ok.entity(RegistrationService.failingClients()).build
  }

  @GET
  @Path("/clients/dead")
  @ApiOperation(value = "Clients removed from the cluster",
    response = classOf[ServiceNode],
    responseContainer = "List")
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "TODO"))
  )
  def deadClients (
    @ApiParam(value = "Authorization Code", required = true)@HeaderParam("access_token") accessToken: String) = {
    Response.ok.entity(RegistrationService.deadClients()).build
  }

  @POST
  @Path("/ping")
  @ApiOperation(value = "receives a keepalive",
    response = classOf[ApiResponseMessage])
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "TODO"))
  )
  def nodeKeepAlive(
    @ApiParam(value = "Authorization Code", required = true)@HeaderParam("access_token") accessToken: String,
    @ApiParam(value = "Node to keep alive", required = true) node: ServiceNode) = {
    val response = RegistrationService.receivePing(node)
    Response.ok.entity(response).build
  }

  @GET
  @ApiOperation(value = "gets registered nodes",
    response = classOf[ServiceNode],
    responseContainer = "List")
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "TODO"))
  )
  def registeredNodes(
    @ApiParam(value = "Authorization Code", required = true)@HeaderParam("access_token") accessToken: String) = {
    val data = RegistrationService.getClients.values.asJava
    Response.ok.entity(data).build
  }
}
