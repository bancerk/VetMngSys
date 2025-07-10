# VetMngSys

This repo contains my https://github.com/bancerk/vet-management-system project and my attempt to migrate it from monolithic architecture to microservice architecture to gain familiarity with docker and kubernetes.

Kubernetes is not designed to operate at this project's small scale, so this repo will probably only go as far as a proof-of-concept.

# Suggested migration strategy

This part of the document outlines the strategy for migrating the monolithic Vet Management System to a microservices architecture.

## 1. Service Decomposition Strategy
The first step is to identify the boundaries of the new microservices. Looking at the project's domains (Customer, Animal, Doctor, Appointment, Vaccine), we can decompose the monolith by business capability. Each service will be a self-contained application with its own database.

Here is a potential breakdown:

*   **`customer-service`**:
    *   **Responsibilities**: Manages all customer-related operations (CRUD).
    *   **Entities**: `Customer`.
    *   **API Endpoints**: `/api/v1/customers/**`.

*   **`animal-service`**:
    *   **Responsibilities**: Manages all animal-related data. It will need to communicate with the `customer-service` to validate the existence of a customer when an animal is created or updated.
    *   **Entities**: `Animal`.
    *   **API Endpoints**: `/api/v1/animals/**`.

*   **`scheduling-service`**:
    *   **Responsibilities**: Manages doctors, their availability, and appointments. This service encapsulates the entire scheduling domain. It will need to communicate with the `animal-service` to get animal details for an appointment.
    *   **Entities**: `Doctor`, `AvailableDate`, `Appointment`.
    *   **API Endpoints**: `/api/v1/doctors/**`, `/api/v1/available-dates/**`, `/api/v1/appointments/**`.

*   **`vaccine-service`**:
    *   **Responsibilities**: Manages all vaccine records for animals, including business logic for expiry dates. It will need to communicate with the `animal-service` to link vaccines to a specific animal.
    *   **Entities**: `Vaccine`.
    *   **API Endpoints**: `/api/v1/vaccines/**`.

## 2. Core Architectural Components
A distributed system requires several new components to function effectively.

*   **API Gateway**:
    *   **Purpose**: A single entry point for all client requests. It routes incoming requests to the appropriate microservice.
    *   **Benefits**: Simplifies the client, provides a central place for cross-cutting concerns like authentication (e.g., using JWT), rate limiting, and SSL termination.
    *   **Example Tech**: Spring Cloud Gateway

*   **Service Registry & Discovery**:
    *   **Purpose**: Allows services to find and communicate with each other without hardcoding hostnames and ports. Each microservice registers itself with the registry on startup.
    *   **Example Tech**: Netflix Eureka, Consul, Zookeeper.

*   **Centralized Configuration**:
    *   **Purpose**: Manages configuration properties for all microservices in a central location. This avoids scattering configuration across different services and allows for dynamic updates.
    *   **Example Tech**: Spring Cloud Config Server

*   **Inter-Service Communication**:
    *   **Synchronous (REST APIs)**: For direct request/response interactions. For example, when creating an animal, the `animal-service` would make a synchronous call to the `customer-service` to verify the customer ID. Spring's `WebClient` or `RestTemplate` can be used for this.

    *   **Asynchronous (Message Broker)**: For event-driven communication, which decouples services and improves resilience. For example, if a customer is deleted, the `customer-service` could publish a `CustomerDeleted` event. The `animal-service` would subscribe to this event and handle the deletion of associated pets.
    *   **Example Tech**: RabbitMQ, Apache Kafka.

## 3. Data Management Strategy
A core principle of microservices is that each service owns its own data.

*   **Database per Service**: Each microservice (`customer-service`, `animal-service`, etc.) will have its own dedicated database. This prevents tight coupling at the database level.

*   **Data Consistency**: With distributed data, you lose ACID transaction guarantees across services. You'll need to manage consistency using patterns like:

    *   **Eventual Consistency**: The system becomes consistent over time. This is often achieved with asynchronous messaging.

    *   **Saga Pattern**: A sequence of local transactions. If one transaction fails, the saga executes compensating transactions to undo the preceding ones.

## 4. Phased Migration Plan (The Strangler Fig Pattern)
Instead of a "big bang" rewrite, you can migrate incrementally. The Strangler Fig Pattern is perfect for this.

1.  **Identify a Candidate for Extraction**: Start with a part of the application that has fewer dependencies. In your case, `CustomerController` and its related logic is a great first candidate.


2.  **Create the First Microservice**: Build the new `customer-service` as a separate Spring Boot application with its own database.

3.  **Introduce the API Gateway**: Set up an API Gateway. Configure it to route all requests for `/customer/**` to the new `customer-service`. All other requests still go to the original monolith.

5.  **Update the Monolith**: The monolith now becomes a client of the new `customer-service`. Where `AnimalManager` used to call `customerRepo.findById()`, it will now make an HTTP request to the `customer-service` via the service discovery mechanism.

7.  **Repeat**: Continue this process for the next domain. For example, extract the `animal` functionality into an `animal-service`. Now, the monolith and the `vaccine-service` (once it's created) will call the `animal-service` for animal data.

9.  **Strangle the Monolith**: As more functionality is moved into microservices, the original monolithic application shrinks. Eventually, all its features will be handled by the new services, and the monolith can be decommissioned.

This outline provides a strategic roadmap for the migration. The key is to proceed incrementally, ensuring the system remains functional at every step.
