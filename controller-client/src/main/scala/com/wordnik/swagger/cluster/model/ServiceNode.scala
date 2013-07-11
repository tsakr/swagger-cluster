package com.wordnik.swagger.cluster.model

case class ServiceNode (
  nodeType: String,
  resourcePath: String,
  privateResourceLocation: String,
  publicResourceLocation: String)

