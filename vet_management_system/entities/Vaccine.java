package dev.patika.vet_management_system.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity // Marks this class as a JPA entity
@Table(name = "vaccines") // Maps this entity to the "vaccines" table
@Getter // Lombok: generates getter methods
@Setter // Lombok: generates setter methods
@NoArgsConstructor // Lombok: generates a no-argument constructor
@AllArgsConstructor // Lombok: generates an all-argument constructor
@ToString // Lombok: generates a toString method
@EqualsAndHashCode // Lombok: generates equals and hashCode methods
public class Vaccine {
    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID value using identity strategy
    private Long id;

    @Column(nullable = false) // Maps to a non-null column in the database
    private String name;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDate protectionStartDate;

    @Column(nullable = false)
    private LocalDate protectionFinishDate;

    // Helper method to check if this vaccine is eligible for registration (expiration > 6 months away)
    public boolean isRegistrationAllowed() {
        LocalDate sixMonthsLater = LocalDate.now().plusMonths(6);
        return this.protectionFinishDate.isAfter(sixMonthsLater);
    }

    @ManyToOne(fetch = FetchType.LAZY) // Many vaccines can belong to one animal
    @JoinColumn(name = "animal_id", nullable = false) // Foreign key column for animal
    @ToString.Exclude // Exclude from toString to prevent recursion
    @EqualsAndHashCode.Exclude // Exclude from equals/hashCode to prevent recursion
    private Animal animal;
}
