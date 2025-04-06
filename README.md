# 5E-DRS API

A RESTful API for managing Dungeons & Dragons 5th Edition character data, classes, spells, and other game resources. This API serves as a backend for DRS (Digital RPG Sheet) applications.

## Overview

The 5E-DRS API provides a centralized service for storing, retrieving, and managing D&D 5E game data. It supports pagination, rate limiting, and caching for optimal performance.

## System Architecture

The application is built with a microservice architecture and consists of the following components:

- **RESTful API**: Spring Boot application exposing endpoints for D&D resources
- **MongoDB**: NoSQL database storing all game data
- **RabbitMQ**: Message broker for asynchronous processing
- **Caching**: Caffeine for local caching to improve response times
- **Rate Limiting**: Bucket4j for API request throttling

## Technologies

- Java 17
- Spring Boot 3
- Spring Data MongoDB
- Spring AMQP (RabbitMQ)
- Bucket4j (Rate Limiting)
- Caffeine Cache
- Swagger/OpenAPI for documentation
- Docker & Docker Compose for containerization

## Running the Application

### Prerequisites

- Docker and Docker Compose
- Java 17
- Maven

### Local Development Environment

1. Start the required infrastructure services:

```bash
docker-compose up -d
```

This will start:
- MongoDB (accessible at localhost:27017)
- RabbitMQ (accessible at localhost:5672, management console at localhost:15672)

2. Run the application with the local profile:

```bash
mvn clean package
java -jar -Dspring.profiles.active=local target/5e-drs-api-1.jar
```

Alternatively, you can run the API in Docker by uncommenting the `drs-api` service in the `docker-compose.yml` file and running:

```bash
docker-compose up -d
```

### Environment Configuration

The application supports multiple environments through Spring profiles:
- `local` - For local development
- `prod` - For production deployment

## API Documentation

Once the application is running, you can access:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI definition: http://localhost:8080/api-docs

## Main Features

- Character class management
- Spell cataloging and searching
- Character data storage and retrieval
- Pagination support for large data sets
- Rate limiting to prevent API abuse
- Caching for improved performance

## Development Guidelines

### Project Structure

```
src/
├── main/
│   ├── java/com/ced/
│   │   ├── config/         # Application configuration
│   │   ├── controller/     # REST controllers
│   │   ├── dto/            # Data transfer objects
│   │   ├── exception/      # Custom exceptions
│   │   ├── filter/         # Request filters
│   │   ├── model/          # Domain models
│   │   ├── repository/     # Data access layer
│   │   ├── service/        # Business logic
│   │   └── util/           # Utility classes
│   └── resources/          # Configuration files
└── test/                   # Unit tests
```

### Testing

The project uses JUnit and Mockito for testing. Run tests with:

```bash
mvn test
```

## CI/CD Pipeline

The application is automatically built, tested, and deployed to Discloud using GitHub Actions when changes are pushed to the main branch.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.