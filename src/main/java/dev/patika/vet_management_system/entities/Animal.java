package dev.patika.vet_management_system.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity // Marks this class as a JPA entity
@Table(name = "animals") // Maps this entity to the "animals" table
@Getter // Lombok: generates getter methods
@Setter // Lombok: generates setter methods
@NoArgsConstructor // Lombok: generates a no-argument constructor
@AllArgsConstructor // Lombok: generates an all-argument constructor
@ToString // Lombok: generates a toString method
@EqualsAndHashCode // Lombok: generates equals and hashCode methods
public class Animal {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID value
    private Long id;

    @Column(nullable = false) // Maps to a non-null column
    private String name;

    @Column(nullable = false)
    private String species;

    @Column(nullable = false)
    private String breed;

    @Column(nullable = false)
    private String gender;

    @Column(name = "colour", nullable = false)
    private String colour;

    @Column(nullable = false)
    @Past // Ensures the date is in the past
    private LocalDate dateOfBirth;

    @ManyToOne(fetch = FetchType.LAZY) // Many animals can belong to one customer
    @JoinColumn(name = "customer_id", nullable = false) // Foreign key column
    @ToString.Exclude // Exclude from toString to prevent recursion
    @EqualsAndHashCode.Exclude // Exclude from equals/hashCode to prevent recursion
    @JsonBackReference // Prevents infinite recursion in JSON serialization
    private Customer customer;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude // Exclude from toString to prevent recursion
    @EqualsAndHashCode.Exclude // Exclude from equals/hashCode to prevent recursion
    private List<Vaccine> vaccineList;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Appointment> appointmentList;

    @PrePersist
    @PreUpdate
    private void validateAndInitialize() {
        // Ensure vaccineList is initialized before saving/updating
        if (vaccineList == null) {
            vaccineList = new ArrayList<>();
        }
        // Ensure appointmentList is initialized before saving/updating
        if (appointmentList == null) {
            appointmentList = new ArrayList<>();
        }
    }

    // Helper method to add a vaccine and set the bidirectional relationship
    public void addVaccine(Vaccine vaccine) {
        if (vaccineList == null) {
            vaccineList = new ArrayList<>();
        }
        vaccineList.add(vaccine);
        vaccine.setAnimal(this);
    }

    // Helper method to remove a vaccine and clear the relationship
    public void removeVaccine(Vaccine vaccine) {
        if (vaccineList != null) {
            vaccineList.remove(vaccine);
            vaccine.setAnimal(null);
        }
    }
}
