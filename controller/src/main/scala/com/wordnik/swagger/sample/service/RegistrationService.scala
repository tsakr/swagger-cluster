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

package com.wordnik.swagger.sample.service

import com.wordnik.swagger.sample.model._

import org.slf4j.LoggerFactory

import akka.actor.ActorSystem
import akka.util.Duration

import java.util.concurrent.TimeUnit
import java.net._

import scala.collection.mutable.{ HashMap, HashSet }
import scala.io.Source.{fromInputStream}

object RegistrationService {
  private val LOGGER = LoggerFactory.getLogger(RegistrationService.getClass)

  schedulePings()

  val clients = new HashMap[String, ServiceNode]
  val failures = new HashMap[String, Int]
  val deadNodes = new HashSet[ServiceNode]

  def getClients = clients.toMap

  def healthyClients() = {
    val keys = (clients.keys.toSet -- failures.keys.toSet)
    (for(key <- keys) yield clients(key)).toList.sortWith(_.publicResourceLocation < _.publicResourceLocation)
  }

  def failingClients() = {
    (for(key <- failures.keys) yield clients(key)).toList.sortWith(_.publicResourceLocation < _.publicResourceLocation)
  }

  def deadClients() = {
    (for(node <- deadNodes) yield node).toList.sortWith(_.publicResourceLocation < _.publicResourceLocation)
  }

  def schedulePings() = {
    val system = akka.actor.ActorSystem("system")
    system.scheduler.schedule(Duration.create(10, TimeUnit.SECONDS), Duration.create(30, TimeUnit.SECONDS), new Runnable {
      def run() = try {
        sendPings()
      }
      catch {
        case e: Exception => e.printStackTrace
      }
    })
  }

  def sendPings() = {
    clients.map(m => {
      val node = m._2
      val address = node.privateResourceLocation
      LOGGER.debug("pinging " + address)
      try {
        val urlCon = new URL(address).openConnection()
        urlCon.setConnectTimeout(2000)
        urlCon.setReadTimeout(1000)
        val content = fromInputStream( urlCon.getInputStream ).getLines.mkString("\n")
        if(content != "") {
          LOGGER.debug("successful ping to " + address)
          if(failures.contains(node.privateResourceLocation))
            failures.remove(node.privateResourceLocation)
          if(deadNodes.contains(node))
            deadNodes.remove(node)
        }
      }
      catch {
        case e: Exception => {
          val failedCount = failures.getOrElse(node.privateResourceLocation, 0) + 1

          LOGGER.debug("can't reach " + address + " after " + failedCount + " retries")
          failures += node.privateResourceLocation -> failedCount

          if(failedCount >= 10) {
            LOGGER.debug("killing dead node " + node.privateResourceLocation)
            clients.remove(node.privateResourceLocation)
            failures.remove(node.privateResourceLocation)
            deadNodes += node
          }
        }
      }
    })
  }

  def receivePing(node: ServiceNode) = {
    if(clients.contains(node.privateResourceLocation))
      ApiResponseMessage(200, "ping received")
    else
      ApiResponseMessage(401, "not authorized")
  }

  def register(node: ServiceNode) = {
    clients.filter(_._1 == node.privateResourceLocation).size match {
      case 0 => {
        clients += node.privateResourceLocation -> node
        ApiResponseMessage(200, "%s successfully registered".format(node.privateResourceLocation))
      }
      case e: Int => {
        LOGGER.debug("overwriting registration for " + node)
        clients += node.privateResourceLocation -> node
        ApiResponseMessage(200, "re-registered node %s".format(node.privateResourceLocation))
      }
    }
  }

  def services = {
    val addresses = (clients.map(m => {
      m._2.privateResourceLocation
    })).toList

    addresses
  }
}
