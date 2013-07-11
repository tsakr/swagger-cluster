# Swagger Cluster Sample

## Need more info about Swagger?  [See the Wiki!](https://github.com/wordnik/swagger-core/wiki)
The [github wiki](https://github.com/wordnik/swagger-core/wiki) contains documentation, samples, etc.  Start there, for everyone's good.

## Where to get help!
Search the [swagger google groups](https://groups.google.com/forum/#!forum/swagger-swaggersocket) for previously 
asked questions.  Join #swagger on irc.freenode.net to talk to interesting human beings.  And if you find a bug,
file it in github:

* [Swagger core + server itegrations](https://github.com/wordnik/swagger-core/issues) issues
* [Swagger UI](https://github.com/wordnik/swagger-ui/issues)
* [Swagger codegen](https://github.com/wordnik/swagger-codegen/issues)

## What is this project?
Swagger Cluster is a demonstration of how swagger can help build an internal cluster of dissimilar servers, built in different languages.  The Swagger Specification allows the servers to talk amongst themselves.

Some of the key components in this system:

### Controller
The controller is a centralized directory of services.  It keeps track of the running instances via heartbeats and provides a simple mechanism to detect failing or dead services.  In addition, it provides a single interface to query each of the services via swagger-ui.

When services start up, they look for the controller and register themselves.  Registration invokes an oAuth 2 server flow, which prevents rogue services from becoming part of the grid.

### Controller-client
This is a client library generated with swagger-codegen.  The client library is invoked as such:

```
mvn scala:run -Dlauncher=codegen
```

This will start the controller service (assuming it has been built) and generate the client source.  You then can package and install it locally:

```
mvn install
```

### Cluster-node
This is a client library which should be included in any java-based service communicating in the cluster.  It performs the registration process for the nodes by invoking the `com.reverb.swagger.cluster.client.RegisterBean`.

### Node1
This is a single node running Jetty and Apache CXF.  It provides a single resource on port 8080, the `pet` api.

### Node2
This is a single node running Tomcat6 and Apache CXF.  It provides the `user` api on port 8090

### Node3
This is a single node running Tomcat6 and Apache CXF.  It provides the `store` api on port 9000


## Running the cluster
You can start the servers up in any order, they will discover each other and become available when ready, but here's a guideline.

```
# start the controller
cd controller
mvn clean package jetty:run

# start node1
cd node1
mvn clean package jetty:run

# start node2
cd node2
mvn clean package tomcat6:run

# start node3
cd node3
mvn clean package tomcat6:run
```

You can now open a browser and view http://localhost:8002 to see the swagger ui for the cluster.  Click the refresh button and select any of the running services in the list.

Note there is a special service in the list, the controller.  For this instance, you can see all the available operations for the controller, such as getting running serivces, failing services, and dead services.  Pretty handy for updating operational tasks like nagios monitoring, etc!


## Doing real stuff

Try stopping a node.  Once down, it will be removed from the cluster (hit refresh in the UI).  Start a server--you'll see it come up and become active in the system.