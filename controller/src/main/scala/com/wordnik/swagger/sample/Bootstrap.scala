package com.wordnik.swagger.sample

import com.wordnik.swagger.jaxrs._
import com.wordnik.swagger.config._
import com.wordnik.swagger.model._

import javax.servlet.http.HttpServlet

class Bootstrap extends HttpServlet {
  val info = ApiInfo(
    title = "Swagger Cluster Controller",
    description = """This is a server which manages instances in a cluster.  You can find out more about Swagger 
    at <a href="http://swagger.wordnik.com">http://swagger.wordnik.com</a> or on irc.freenode.net, #swagger.""", 
    termsOfServiceUrl = "http://helloreverb.com/terms/",
    contact = "apiteam@wordnik.com", 
    license = "Apache 2.0", 
  	licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.html")
  ConfigFactory.config.info = Some(info)
}