package dev.patika.vet_management_system.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.*;

import java.time.LocalDateTime;

@Entity // Marks this class as a JPA entity
@Table(name = "appointments") // Maps this entity to the "appointments" table
@Getter // Lombok: generates getter methods
@Setter // Lombok: generates setter methods
@NoArgsConstructor // Lombok: generates a no-argument constructor
@AllArgsConstructor // Lombok: generates an all-argument constructor
@ToString // Lombok: generates a toString method
@EqualsAndHashCode // Lombok: generates equals and hashCode methods
public class Appointment {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID value using identity strategy
    private Long id;

    @Column(nullable = false) // Maps to a non-null column in the database
    @Future // Ensures the appointment date is in the future
    private LocalDateTime appointmentDate;

    @ManyToOne(fetch = FetchType.EAGER) // Many appointments can be for one animal
    @JoinColumn(name = "animal_id", nullable = false) // Foreign key column for animal
    @ToString.Exclude // Exclude from toString to prevent recursion
    @EqualsAndHashCode.Exclude // Exclude from equals/hashCode to prevent recursion
    private Animal animal;

    @ManyToOne(fetch = FetchType.EAGER) // Many appointments can be for one doctor
    @JoinColumn(name = "doctor_id", nullable = false) // Foreign key column for doctor
    @ToString.Exclude // Exclude from toString to prevent recursion
    @EqualsAndHashCode.Exclude // Exclude from equals/hashCode to prevent recursion
    private Doctor doctor;

    @PrePersist
    @PreUpdate
    private void validateAppointmentTime() {
        // Ensure appointments are scheduled exactly on the hour (minute and second must be zero)
        if (appointmentDate.getMinute() != 0 || appointmentDate.getSecond() != 0) {
            throw new IllegalArgumentException("Appointments must be on the hour.");
        }
    }
}
