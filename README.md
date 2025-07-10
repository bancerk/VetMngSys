# Vet Management System

A Spring Boot application for managing veterinary clinic operations, including animals, customers, doctors, appointments, vaccines, and available dates.

## Features

- Manage animals, customers, doctors, vaccines, and available dates.
- RESTful API endpoints for CRUD operations.
- Data validation and error handling.
- PostgreSQL database integration.
- Uses ModelMapper for DTO/entity mapping.
- Layered architecture (entity, repository, service, manager, DTO, controller).
- Advanced filtering and business rules for vaccines and appointments.

## Project Structure

```
src/
  main/
    java/
      dev/patika/vet_management_system/
        api/           # REST controllers
        business/      # Service and manager classes
        core/          # Core utilities and exception handling
        dao/           # Spring Data JPA repositories
        dto/           # Data Transfer Objects (DTOs)
        entities/      # JPA entities
    resources/
      application.yaml # Main configuration
```

## Getting Started

### Prerequisites

- Java 21
- Maven
- PostgreSQL

### Setup

1. **Clone the repository:**
   ```sh
   git clone https://github.com/yourusername/vet-management-system.git
   cd vet-management-system
   ```

2. **Configure the database:**
   - Create a PostgreSQL database named `vet-management-system`.
   - Update the credentials in [`src/main/resources/application.yaml`](src/main/resources/application.yaml) accordingly.

3. **Build the project:**
   ```sh
   ./mvnw clean install
   ```

4. **Run the application:**
   ```sh
   ./mvnw spring-boot:run
   ```
   The server will start on port `8085` by default. To change the server port edit [`src/main/resources/application.yaml`](src/main/resources/application.yaml) accordingly.

## API Endpoints

### Animal

- `GET /animal?name={name}`  
  Get animal details by name.
- `GET /animal/{id}`  
  Get animal details by ID.
- `POST /animal/save`  
  Create a new animal.
- `PUT /animal/update`  
  Update animal details.
- `DELETE /animal/delete/{id}`  
  Delete an animal by ID.
- `GET /animal/vaccines?name={animalName}`  
  List all vaccines for an animal by name.

### Customer

- `GET /customer/{id}`  
  Get customer details by ID.
- `GET /customer?name={name}`  
  Get customer details by name.
- `POST /customer/save`  
  Create a new customer.
- `PUT /customer/update`  
  Update customer details.
- `DELETE /customer/delete/{id}`  
  Delete a customer by ID.
- `GET /customer/{id}/animals`  
  List all animals belonging to a customer.

### Doctor

- `GET /doctor/{id}`  
  Get doctor details by ID.
- `POST /doctor/save`  
  Create a new doctor.
- `PUT /doctor/update`  
  Update doctor details.
- `DELETE /doctor/delete/{id}`  
  Delete a doctor by ID.

### Appointment

- `GET /appointment?appointmentDate={yyyy-MM-ddTHH:mm:ss}`  
  List appointments by date.
- `GET /appointment?animalName={animalName}`  
  List appointments by animal name.
- `GET /appointment?doctorName={doctorName}`  
  List appointments by doctor name.
- `POST /appointment/save`  
  Create a new appointment.
- `PUT /appointment/update`  
  Update appointment details.
- `DELETE /appointment/delete/{id}`  
  Delete an appointment by ID.

### Vaccine

- `GET /vaccine/{id}`  
  Get vaccine details by ID.
- `POST /vaccine/save`  
  Create a new vaccine (only allowed if protection finish date is more than 6 months away).
- `PUT /vaccine/update`  
  Update vaccine details.
- `DELETE /vaccine/delete/{id}`  
  Delete a vaccine by ID.
- `GET /vaccine/expiring-in-two-weeks`  
  List animals with at least one vaccine expiring in the next two weeks (includes animal and vaccine details).
- `GET /vaccine/animal/{animalName}`  
  List all vaccines for a given animal by name.

### Available Date

- `GET /availabledate/{id}`  
  Get available date by ID.
- `POST /availabledate/save`  
  Create a new available date.
- `PUT /availabledate/update`  
  Update available date.
- `DELETE /availabledate/delete/{id}`  
  Delete an available date by ID.

## Business Rules

- **Vaccine Registration:**  
  Vaccines can only be registered if their protection finish date is more than 6 months from today.
- **Vaccine Expiry Reporting:**  
  `/vaccine/expiring-in-two-weeks` returns animals and vaccine info for vaccines expiring within the next 14 days.
- **Appointment Filtering:**  
  Appointments can be filtered by date, animal name, or doctor name using query parameters.

## Technologies Used

- Java 21
- Spring Boot 3.5.0
- Spring Data JPA
- PostgreSQL
- Lombok
- ModelMapper

## API Testing

A Postman collection is provided at [`src/main/resources/vet-management-system.postman_collection.json`](src/main/resources/vet-management-system.postman_collection.json).

---

For more details, see the code and comments in each controller and DTO.
