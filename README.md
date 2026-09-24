Task Management API

A RESTful Task Management API built with **Java 17 and Spring Boot**.

Features

* Create a task
* Get all tasks
* Get a task by ID
* Update a task
* Delete a task
* MySQL database integration
* DTO-based request and response handling
* Global exception handling
* Pagination
* Case-insensitive title search
* Swagger/OpenAPI documentation
* Automated unit and controller tests
* Docker containerization

Tech Stack

* Java 17
* Spring Boot
* Spring Data JPA
* MySQL
* Maven
* JUnit 5
* Mockito
* MockMvc
* Swagger / OpenAPI
* Docker
* Git and GitHub

API Documentation

Swagger UI is available at:

http://localhost:8080/swagger-ui/index.html

Use Swagger to test the available CRUD, search, and pagination endpoints.

Running Locally

1. Configure MySQL

Create a MySQL database named:

task_management

Set your MySQL password as an environment variable.

PowerShell:

$env:DB_PASSWORD="your_mysql_password"

2. Run the application

.\mvnw spring-boot:run

The API will start on:

http://localhost:8080

3. Run tests

.\mvnw test

Running with Docker

Build the application JAR:

.\mvnw clean package -DskipTests

Build the Docker image:

docker build -t task-management-api .

Run the container:

docker run --name task-management-api -p 8080:8080 -e DB_HOST=host.docker.internal -e DB_PASSWORD="$env:DB_PASSWORD" task-management-api

The application will then be available at:

http://localhost:8080

Swagger:

http://localhost:8080/swagger-ui/index.html

The Dockerized application connects to MySQL running on the host machine through host.docker.internal.

Project Structure

src
├── main
│   ├── java
│   │   └── com.keerthanaa.task_management_api
│   │       ├── controller
│   │       ├── dto
│   │       ├── entity
│   │       ├── exception
│   │       ├── mapper
│   │       ├── repository
│   │       └── service
│   └── resources
│       └── application.properties
└── test
    └── java
        └── com.keerthanaa.task_management_api

Testing

The project includes automated tests covering:

* Service layer
* Controller layer
* Exception handling
* REST API behaviour

Run all tests with:

.\mvnw test

GitHub

Source code is maintained using Git and GitHub.
