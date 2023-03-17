# PARCEL DELIVERY APP

## Technologies
- java 11
- Maven
- Spring Boot
- Spring Security with JWT Token
- Spring Data JPA
- Postgres
- Liquibase
- Spring Cloud Gateway
- Eureka
- Rabbitmq
- Docker
- SpringDoc OpenAPI

## About app

## Architecture

![Architecture](/img/architecture.PNG)


### Auth Service
The service is used to authenticate users. The service generates JWT tokens.

### Order Service
The service is used to create and manage orders. It raises events to delivery service.

### Delivery Service
The service is used to manage parcel deliveries. It interacts with the order service.


### Users
There are already created users in the database

| username            | password | role         |
|---------------------|----------|--------------|
| user@user.com       | 123456   | ROLE_USER    |
| admin@admin.com     | 123456   | ROLE_ADMIN   |
| courier@courier.com | 123456   | ROLE_COURIER |


### Docs
http://localhost:8765/swagger-ui/index.html

![Architecture](/img/auth.PNG)
![Architecture](/img/order.PNG)
![Architecture](/img/delivery.PNG)


## Getting Started
Run *docker-compose.yml* using Docker

``` docker compose up```
