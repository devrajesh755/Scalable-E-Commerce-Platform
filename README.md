# E-Commerce Microservices Application

A Spring Boot--based e-commerce backend built using a **microservices
architecture**. The project is organized into independent services for
user, product, order, and store-related operations, with centralized
configuration and service discovery.

> **Note:** This README is based on the project structure shown in the
> screenshot. Update service ports, endpoint paths, database names, and
> environment variables to match your actual implementation.

------------------------------------------------------------------------

## Table of Contents

-   [Project Overview](#project-overview)
-   [Architecture](#architecture)
-   [Project Structure](#project-structure)
-   [Technology Stack](#technology-stack)
-   [Microservices](#microservices)
-   [Inter-Service Communication with Feign
    Client](#inter-service-communication-with-feign-client)
-   [Request Validation](#request-validation)
-   [Prerequisites](#prerequisites)
-   [Getting Started](#getting-started)
-   [Running with Docker Compose](#running-with-docker-compose)
-   [Configuration](#configuration)
-   [API Documentation and Testing](#api-documentation-and-testing)
-   [Typical Request Flow](#typical-request-flow)
-   [Troubleshooting](#troubleshooting)
-   [Future Enhancements](#future-enhancements)

------------------------------------------------------------------------

## Project Overview

This application separates e-commerce functionality into independently
deployable Spring Boot services. Each service is responsible for a
specific business capability and can communicate with other services
through REST APIs.

The repository includes:

-   **Config Server** --- centralized application configuration
-   **Eureka Server** --- service registration and discovery
-   **User Service** --- user-related operations
-   **Product Service** --- product-related operations
-   **Order Service** --- order-related operations
-   **Store Services** --- store-related operations
-   **Docker Compose** --- local multi-container orchestration

The application is designed to support service-to-service communication
using **Spring Cloud OpenFeign**. Request validation can be applied at
API boundaries using Jakarta Bean Validation.

## Architecture

``` mermaid
flowchart TD
    Client[Client / Postman / Frontend]

    subgraph Platform["Spring Cloud Infrastructure"]
        Config[Config Server]
        Eureka[Eureka Discovery Server]
    end

    subgraph Services["E-Commerce Microservices"]
        User[User Service]
        Product[Product Service]
        Order[Order Service]
        Store[Store Services]
    end

    Client --> User
    Client --> Product
    Client --> Order
    Client --> Store

    User -. registers .-> Eureka
    Product -. registers .-> Eureka
    Order -. registers .-> Eureka
    Store -. registers .-> Eureka

    User -. loads config .-> Config
    Product -. loads config .-> Config
    Order -. loads config .-> Config
    Store -. loads config .-> Config
    Eureka -. loads config .-> Config

    Order -->|Feign Client / REST| User
    Order -->|Feign Client / REST| Product
    Order -->|Feign Client / REST| Store
```

### Architecture Summary

1.  The Config Server provides externalized configuration to the
    services.
2.  Eureka Server maintains the registry of available service instances.
3.  Each microservice registers with Eureka, if Eureka client support is
    configured.
4.  A service that needs another service's API can call it through a
    Feign Client.
5.  Incoming request data is validated before business logic is executed
    when validation annotations are configured.

------------------------------------------------------------------------

## Project Structure

``` text
Ecom-application/
├── configserver/       # Centralized configuration service
├── eurea-server/       # Eureka service-discovery server
├── order/              # Order microservice
├── product/            # Product microservice
├── storeServices/      # Store-related microservice(s)
├── user/               # User microservice
├── docker-compose.yml  # Local container orchestration
└── README.md
```

Each service folder is expected to contain its own Spring Boot
application and build configuration (for example, `pom.xml`). The exact
internal package structure may differ.

------------------------------------------------------------------------

## Technology Stack

  -----------------------------------------------------------------------
Technology                          Purpose
  ----------------------------------- -----------------------------------
Java                                Application programming language

Spring Boot                         Building standalone REST services

Spring Cloud Config                 Centralized configuration

Netflix Eureka                      Service registration and discovery

Spring Cloud OpenFeign              Declarative REST communication
between services

Jakarta Bean Validation             Validating incoming request data

Maven                               Dependency management and build

Docker                              Packaging services into containers

Docker Compose                      Running multiple containers locally

Postman / curl                      API testing
-----------------------------------------------------------------------

Only list technologies that are actually configured in your project. Add
the database, API documentation, security, or monitoring tools you use.

------------------------------------------------------------------------

## Microservices

### 1. Config Server (`configserver`)

Provides centralized configuration to the application services.

**Responsibilities** - Loads external configuration from the configured
source. - Supplies service-specific properties, such as ports and
service URLs. - Helps keep environment-specific configuration outside
service code.

### 2. Eureka Server (`eurea-server`)

Acts as the service registry for the microservices.

**Responsibilities** - Accepts service registrations. - Maintains
information about registered service instances. - Allows
discovery-enabled clients to locate services by application name.

### 3. User Service (`user`)

Manages user-related business operations.

Possible responsibilities include creating, retrieving, updating, or
deleting user records. Document the exact operations implemented by your
service.

### 4. Product Service (`product`)

Manages product-related operations, such as product creation, retrieval,
updates, and inventory details, depending on the implementation.

### 5. Order Service (`order`)

Manages order-related operations. It may use Feign Clients to retrieve
user, product, or store information from the corresponding services
before processing an order.

### 6. Store Services (`storeServices`)

Contains store-related functionality. Update this section if the folder
contains multiple applications or has a more specific responsibility.

------------------------------------------------------------------------

## Inter-Service Communication with Feign Client

**Spring Cloud OpenFeign** lets a service call another service's HTTP
API through a Java interface. It reduces the need to write repetitive
`RestTemplate` or low-level HTTP client code.

For example, the Order Service may call the Product Service to retrieve
product information.

### 1. Add the dependency

For Maven, add the Spring Cloud OpenFeign starter that matches your
Spring Boot and Spring Cloud release-train versions:

``` xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

Use the Spring Cloud BOM recommended for your Spring Boot version; avoid
selecting a release train at random.

### 2. Enable Feign Clients

Add `@EnableFeignClients` to the relevant Spring Boot application class
(or a configuration class):

``` java
@SpringBootApplication
@EnableFeignClients
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
```

### 3. Create a Feign Client

The following is an **example**. Change the service name, route, and DTO
to match the Product Service API.

``` java
@FeignClient(name = "product")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductResponse getProductById(@PathVariable("id") Long id);
}
```

With Eureka-based discovery, `name = "product"` should match the Product
Service's registered application name. The exact setup depends on your
configuration.

### 4. Inject and use the client

``` java
@Service
public class OrderService {

    private final ProductClient productClient;

    public OrderService(ProductClient productClient) {
        this.productClient = productClient;
    }

    public ProductResponse getProduct(Long productId) {
        return productClient.getProductById(productId);
    }
}
```

### Feign Client Flow

``` text
Order Service
     |
     | calls ProductClient.getProductById(id)
     v
Feign Client
     |
     | discovers "product" through Eureka
     v
Product Service REST API
     |
     | returns product response
     v
Order Service
```

Feign Client handles the HTTP request mapping. It does **not**
automatically validate business data or guarantee that the remote
service is healthy. Configure appropriate timeouts, error handling, and
(if needed) a circuit breaker.

------------------------------------------------------------------------

## Request Validation

Use **Jakarta Bean Validation** to validate incoming request data before
it reaches the business logic. Validation is commonly applied to request
DTOs in the controller layer.

### 1. Add the validation dependency

``` xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

### 2. Add validation annotations to a DTO

Example request DTO:

``` java
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderRequest {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Long userId;

    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    private Long productId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    // Getters and setters
}
```

### 3. Trigger validation in the controller

``` java
@PostMapping("/api/orders")
public ResponseEntity<?> createOrder(
        @Valid @RequestBody OrderRequest request) {

    return ResponseEntity.ok(orderService.createOrder(request));
}
```

`@Valid` tells Spring to validate the request DTO. If the input violates
a constraint, Spring raises a validation exception instead of proceeding
normally to the controller method.

### Validation Flow

``` text
Client sends JSON
       |
       v
Controller receives request
       |
       v
@Valid triggers DTO validation
       |
       +---- Invalid ----> 400 Bad Request + validation details
       |
       +---- Valid ------> Service layer / business logic
```

### Important Validation Notes

-   Use `@NotBlank` for strings that must contain non-whitespace text.
-   Use `@NotNull` when a value must not be null.
-   Use `@Positive` for IDs or values that must be greater than zero.
-   Use `@Min` / `@Max` for numeric limits.
-   Use `@Email` for email-format validation.
-   DTO validation checks request shape and constraints; it does not
    replace business validation.
-   For example, verify through the relevant service that a user or
    product actually exists before creating an order.
-   Avoid exposing stack traces or sensitive internal details in API
    error responses.

------------------------------------------------------------------------

## Prerequisites

Install the tools required by your project's versions:

-   JDK compatible with your Spring Boot version
-   Maven (or use the Maven Wrapper if included)
-   Docker Engine or Docker Desktop
-   Docker Compose
-   Git
-   Postman or curl for API testing

Verify installations:

``` bash
java -version
mvn -version
docker --version
docker compose version
```

------------------------------------------------------------------------

## Getting Started

### 1. Clone the repository

``` bash
git clone <your-repository-url>
cd Ecom-application
```

Replace `<your-repository-url>` with your repository URL.

### 2. Configure application properties

Review each service's `application.yml` / `application.properties` and
the Config Server's configuration source. Confirm:

-   Service application names
-   Server ports
-   Config Server URL
-   Eureka Server URL
-   Database connection settings, if applicable
-   Feign client names and routes

Do not commit passwords, tokens, or production credentials. Use
environment variables or a secrets manager.

### 3. Build the services

Run Maven from each service directory:

``` bash
cd configserver
mvn clean package
```

Repeat for `eurea-server`, `user`, `product`, `order`, and
`storeServices` (where each folder contains a Maven project). If a Maven
Wrapper is present, you can use `./mvnw` on Linux/macOS or `mvnw.cmd` on
Windows.

### 4. Start the services

Start infrastructure components first, then the business services. If
running manually, use separate terminals and ensure each service's
configuration is correct.

------------------------------------------------------------------------

## Running with Docker Compose

The repository contains `docker-compose.yml` for running the application
as containers.

From the repository root:

``` bash
docker compose config
docker compose up --build -d
```

Check container status and logs:

``` bash
docker compose ps
docker compose logs -f
```

Stop the containers:

``` bash
docker compose down
```

To also remove named volumes (this deletes persisted container data),
use:

``` bash
docker compose down -v
```

Review the Compose file before running. Confirm that build contexts,
Dockerfile paths, ports, environment variables, service dependencies,
and health checks match the actual project. `depends_on` alone does not
necessarily mean an application is ready to accept requests.

------------------------------------------------------------------------

## Configuration

Keep environment-specific settings external to the code. Typical
properties (illustrative only) include:

``` yaml
spring:
  application:
    name: order

eureka:
  client:
    service-url:
      defaultZone: ${EUREKA_SERVER_URL:http://localhost:8761/eureka}
```

The actual property names, ports, and service identifiers must match
your Config Server and service configuration. Do not copy this example
blindly if your project uses a different setup.

------------------------------------------------------------------------

## API Documentation and Testing

Use Postman, curl, or your configured API documentation tool to test
each service.

Example request (replace the host, port, and endpoint with your actual
values):

``` bash
curl -X POST http://localhost:<order-port>/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 101,
    "quantity": 2,
    "shippingAddress": "Kolkata"
  }'
```

Test both valid and invalid requests:

-   Valid request returns the expected success response.
-   Missing required fields trigger validation errors.
-   Invalid values (such as a negative ID or quantity) are rejected.
-   Unknown user/product IDs are handled by business logic.
-   A downstream service failure is handled gracefully.

If Springdoc OpenAPI or Swagger UI is configured, add its actual URL
here.

------------------------------------------------------------------------

## Typical Request Flow

A typical order workflow may look like this (adapt it to your actual
implementation):

1.  Client submits an order request to the Order Service.
2.  The Order Service validates the incoming DTO.
3.  The Order Service uses Feign Clients to request user/product/store
    information when required.
4.  The relevant services return their responses.
5.  The Order Service applies business rules and processes the order.
6.  The Order Service returns the result to the client.

------------------------------------------------------------------------

## Troubleshooting

  ---------------------------------------------------------------------------
Problem                             What to check
  ----------------------------------- ---------------------------------------
Service cannot connect to Config    Config Server address, startup order,
Server                              profile and configuration import

Service does not appear in Eureka   Eureka URL, application name, network
connectivity and client dependency

Feign returns 404                   Target service route, HTTP method, path
variables and context path

Feign cannot find a service         Registered Eureka application name and
discovery configuration

Request validation is not triggered Validation starter dependency,
`@Valid`, and constraints on the DTO

Docker container exits              `docker compose logs <service-name>`,
environment variables and build output

Port is already in use              Change the host port or stop the
process using it
  ---------------------------------------------------------------------------

------------------------------------------------------------------------

## Future Enhancements

Possible improvements, depending on project scope:

-   Add API Gateway for a single entry point.
-   Add centralized exception handling and consistent error responses.
-   Add Feign timeouts, retry policies where appropriate, and
    circuit-breaker support.
-   Add authentication and authorization.
-   Add unit and integration tests.
-   Add health checks, metrics, and distributed tracing.
-   Add CI/CD automation.
-   Add Kubernetes deployment manifests.

------------------------------------------------------------------------

## Author

**Rajesh Manik**\
Java Backend Developer \| Spring Boot \| Microservices \| Docker \|
Kubernetes

Update this README as the implementation evolves, especially the API
endpoints, service names, ports, configuration, and deployment
instructions.
