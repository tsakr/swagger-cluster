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

package com.reverb.swagger.cluster.client

import com.reverb.swagger.cluster.client.executor._

import scala.reflect.BeanProperty

class RegisterBean {
  @BeanProperty var nodeType: String = _
  @BeanProperty var privateResourceLocation: String = _
  @BeanProperty var publicResourceLocation: String = _
  @BeanProperty var controllerAddress: String = _
  @BeanProperty var clientId: String = _
  @BeanProperty var clientSecret: String = _

  def setScan(shouldScan: String) = {
    if("true" == shouldScan) {
      try {
        new RegistrationExecutor()(nodeType, privateResourceLocation, publicResourceLocation, controllerAddress, clientId, clientSecret)
      }
      catch {
        case e: Exception => e.printStackTrace
      }
    }
  }

  def getScan(): String = null
}