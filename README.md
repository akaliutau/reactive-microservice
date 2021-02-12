
[![Generic badge](https://img.shields.io/badge/Build-passing-green.svg)]
[![Generic badge](https://img.shields.io/badge/Language-Java-blue.svg)]

# About

Resource service is a simple microservice with purpose to provide a rudimentary CRUD API to PERSON database

Main features:

 - build on the basis of Spring Boot 2.4.x with extensive use of Reactive Programming patterns (in this project I am using Spring WebFlux)

 - a persistence layer is built on Reactive H2 In-Memory database  

 - contains fine grade access security on the basis of Spring Security

Spring WebFlux is a reactive web framework based on a reactive HTTP layer; such apps can be deployed on Netty or Undertow (with native adapters) or Jetty/Tomcat/any Servlet 3.1. In this implementation I am using Netty. Reactive approach can be beneficial for efficiency and scalability for workloads dealing with lots of latency and concurrency.

As for security, fine-grained authorization from Spring Security's modules allows to secure the business tier through method annotation and the use of interface-based proxies to accomplish AOP. For demonstration purposes I added default user with username=user and password=123 which has two roles – DB_USER and DB_ADMIN.

# Overview

Back-end has only one end-point at `` /api/persons``
Currently five methods are supported with the following signature:

GET /api/persons 

GET /api/persons/{personId}

POST /api/persons

PUT /api/persons

DELETE /api/persons/{personId}

Full API Documentation for back-end server's API is available on http://localhost:9090/api/documentation/swagger-ui/. 

Swagger UI works in the latest versions of Chrome, Safari, Firefox, and Edge.


<br/>


# Installation

## Dockerized build (needs docker to be installed first)

1) The following command builds image with tag resource-service:1.0. Note the dot at the end of the command.

```
docker build -t resource-service:1.0 .
```

2) this command instantiates and expose service at localhost:9090

```
docker run -p 9090:9090 resource-service:1.0
```


## Local build
In order to build and run both back-end the following prerequisites are needed:

[JDK 11](https://openjdk.java.net/)

[Lombok](https://projectlombok.org/download)

Run from the project's root directory:
``mvn clean package ``


<br/>

Run server as follows:

```
java -jar resource-1.0.0-SNAPSHOT.jar
```

Server will start on localhost:9090 address. One can point browser to http://localhost:9090/api/documentation/swagger-ui/ to see API documentation

On first request server will ask to authorize: use the following username/password pair:

Username: user

Password: 123

One can test endpoins through curl as well:

```
curl -X GET "http://localhost:9090/api/persons" --user user:123 -H "accept: */*"
```
