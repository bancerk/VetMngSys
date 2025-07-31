package dev.patika.vet_management_system.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;

import java.time.LocalDate;

@Entity // Marks this class as a JPA entity
@Table(name = "available_dates") // Maps this entity to the "available_dates" table
@Getter // Lombok: generates getter methods
@Setter // Lombok: generates setter methods
@NoArgsConstructor // Lombok: generates a no-argument constructor
@AllArgsConstructor // Lombok: generates an all-argument constructor
@ToString // Lombok: generates a toString method
@EqualsAndHashCode // Lombok: generates equals and hashCode methods
public class AvailableDate {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID value using identity strategy
    private Long id;

    @Column(nullable = false) // Maps to a non-null column in the database
    @FutureOrPresent // Ensures the date is today or in the future
    private LocalDate availableDate;

    @Column(name = "doctor_id", nullable = false) // Foreign key column for doctor
    private Long doctorId;
}
