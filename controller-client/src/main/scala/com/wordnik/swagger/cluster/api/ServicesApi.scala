package com.wordnik.swagger.cluster.api

import com.wordnik.swagger.cluster.client.ApiInvoker
import com.wordnik.swagger.cluster.client.ApiException

import java.io.File

import scala.collection.mutable.HashMap

class ServicesApi {
  var basePath: String = "http://localhost:8002/api"
  var apiInvoker = ApiInvoker
  
  def addHeader(key: String, value: String) = apiInvoker.defaultHeaders += key -> value 

  def registeredNodes (access_token: String) : Option[List[String]]= {
    // create path and map variables
    val path = "/services".replaceAll("\\{format\\}","json")
    val contentType = {
      "application/json"}

    // query params
    val queryParams = new HashMap[String, String]
    val headerParams = new HashMap[String, String]

    // verify required params are set
    (Set(access_token) - null).size match {
       case 1 => // all required values set
       case _ => throw new Exception("missing required params")
    }
    headerParams += "access_token" -> access_token
    try {
      apiInvoker.invokeApi(basePath, path, "GET", queryParams.toMap, None, headerParams.toMap, contentType) match {
        case s: String =>
          Some(ApiInvoker.deserialize(s, "List", classOf[String]).asInstanceOf[List[String]])
        case _ => None
      }
    } catch {
      case ex: ApiException if ex.code == 404 => None
      case ex: ApiException => throw ex
    }
  }
  }

