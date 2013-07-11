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

package com.reverb.swagger.cluster.client.executor

import com.wordnik.swagger.cluster.api._
import com.wordnik.swagger.cluster.model._

import org.slf4j.LoggerFactory

import akka.actor.ActorSystem
import akka.util.Duration

import java.util.concurrent.TimeUnit
import java.net._

import scala.collection.mutable.HashSet

import scala.io._

object RegistrationExecutor {
  private val LOGGER = LoggerFactory.getLogger(RegistrationExecutor.getClass)

  val nodes = new HashSet[(String, ServiceNode)]()
  val api = new RegisterApi

  schedule()

  def schedule() = {
    val system = akka.actor.ActorSystem("system")
    system.scheduler.schedule(Duration.create(10, TimeUnit.SECONDS), Duration.create(30, TimeUnit.SECONDS), new Runnable {
      def run() = try {
        nodes.map(m => ping(m._1, m._2))
      }
      catch {
        case e: Exception => e.printStackTrace
      }
    })
  }

  def ping(controllerAddress: String, node: ServiceNode) = {
    try {
      api.nodeKeepAlive("", node) match {
        case Some(resp) if (resp.code == 200) => {
          LOGGER.debug("successfully pinged controller at " + controllerAddress)
        }
        case _ => {
          LOGGER.error("session expired, resetting")
          register(controllerAddress, node)
        }
      }
    }
    catch {
      case e: Exception => {
        LOGGER.error("Controller is down! " + controllerAddress)
      }
    }
  }

  def register(controllerAddress: String, node: ServiceNode) = {
    nodes += Tuple2(controllerAddress, node)
    // exchange tokens here
    api.basePath = controllerAddress
    api.registerNode ("access_token: String", node) match {
      case Some(e) => {
        if(e.code > 300) {
          LOGGER.error("failed " + e)
        }
        else LOGGER.debug("succeeded " + e)
      }
      case _=> LOGGER.error("error")
    }
  }
}

class RegistrationExecutor {
  def apply(nodeType: String, privateResourceLocation: String, publicResourceLocation: String, controllerAddress: String, clientId: String, clientSecret: String) = {
    val node = ServiceNode (
      resourcePath = "resourcePath",
      nodeType = nodeType,
      privateResourceLocation = privateResourceLocation,
      publicResourceLocation = publicResourceLocation)
    RegistrationExecutor.register(controllerAddress, node)
  }
}
