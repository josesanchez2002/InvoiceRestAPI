# Java Project Guide

## Build & Test Commands
- Build project: `mvn clean install` 
- Run project: `mvn spring-boot:run`
- Run tests: `mvn test`

## Architecture & Code Style
- Language: Java 25
- Maven version: Apache Maven 3.9.16 
- Framework: Spring Boot / JPA
- Formatting: Use standard camelCase, descriptive variable names, and explicit type declarations.
- Error Handling: Always use custom exception classes; avoid raw `RuntimeException`.

## Core Packages & Entry Points
- Main Application: `src/main/java/com/jse/invoice/InvoiceApplication.java`
- Controllers: `src/main/java/com/jse/invoice/controller/`
- Dto: `src/main/java/com/jse/invoice/dto/`
- Entities: `src/main/java/com/jse/invoice/entity/`
- Exception: `src/main/java/com/jse/invoice/exception/`
- Repository: `src/main/java/com/jse/invoice/repository/`
- Service: `src/main/java/com/jse/invoice/service/`
- Utils: `src/main/java/com/jse/invoice/utils/`