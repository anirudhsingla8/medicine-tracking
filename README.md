# Medicine Tracker - Spring Boot Conversion

This project is a Java Spring Boot conversion of the original Node.js Medicine Tracker backend. It provides a full-featured REST API for managing user-specific medicine inventories, multi-user profiles, and dosage schedules.

## Features

*   **Authentication**: Secure user registration and login using JWT.
*   **Multi-Profile Support**: Each user account can manage multiple profiles (e.g., for family members).
*   **Medicine Tracking**: CRUD operations for medicines, including details like dosage, quantity, expiry date, and composition.
*   **Scheduling**: Create and manage dosage schedules for each medicine.
*   **Global Medicine Database**: A pre-populated database of common medicines.
*   **Image Uploads**: Upload medicine images to Cloudinary.
*   **Automated Alerts**: A scheduler runs background jobs to send reminders for dosages, low stock, and expiring medicines (simulated via logging).

## Tech Stack

*   **Java 17**
*   **Spring Boot 3**
*   **Spring Web**: For building REST APIs.
*   **Spring Data JPA**: For data persistence.
*   **Spring Security**: For authentication and authorization.
*   **PostgreSQL**: As the relational database.
*   **Maven**: For dependency management.
*   **Lombok**: To reduce boilerplate code.
*   **Cloudinary**: For cloud-based image storage.

## Project Structure

The project follows a standard layered architecture:

```
src/main/java/com/medicinetracker/
├── config/           # Security configuration, JWT provider
├── controller/       # REST API endpoints
├── dto/              # Data Transfer Objects for API communication
├── exception/        # Global exception handling
├── mapper/           # Mappers for converting between entities and DTOs
├── model/            # JPA entities
├── repository/       # Spring Data JPA repositories
├── service/          # Business logic
└── MedicineTrackerApplication.java  # Main application class
```

## Configuration

Before running the application, you need to set up your configuration in `src/main/resources/application.properties`.

### 1. Database Configuration

Set up your PostgreSQL database and update the following properties:

```properties
spring.datasource.url=jdbc:postgresql://<your-db-host>:<your-db-port>/<your-db-name>
spring.datasource.username=<your-db-user>
spring.datasource.password=<your-db-password>
```

The application will automatically create the schema from the `src/main/resources/schema.sql` file if you are using a new database and `spring.jpa.hibernate.ddl-auto` is set to `create` or `update`. For production, it's recommended to manage the schema manually.

### 2. JWT Configuration

Generate a secure secret key for signing JWTs. You can use an online generator or any other method to create a long, random string.

```properties
# A long, random, base64-encoded string
jwt.secret=your-super-secret-key-that-is-long-and-secure
# Token validity in milliseconds (default is 24 hours)
jwt.expiration-ms=86400000
```

### 3. Cloudinary Configuration

Sign up for a Cloudinary account and find your credentials in the dashboard.

```properties
cloudinary.cloud-name=<your-cloud-name>
cloudinary.api-key=<your-api-key>
cloudinary.api-secret=<your-api-secret>
```

## How to Build and Run

### Prerequisites

*   Java 17 (or higher)
*   Apache Maven
*   A running PostgreSQL instance

### Build

To build the project and package it into a JAR file, run the following command from the root directory:

```bash
mvn clean package
```

### Run

Once the build is complete, you can run the application using:

```bash
java -jar target/medicine-tracker-0.0.1-SNAPSHOT.jar
```

The application will start on port `8080` by default. You can change the port in the `application.properties` file.

## API Documentation

The API endpoints are implemented as described in the original project's `README.md`. You can use a tool like Postman or `curl` to interact with the API. All protected endpoints require a `Bearer` token in the `Authorization` header.
