package dev.patika.vet_management_system.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity // Marks this class as a JPA entity
@Table(name = "doctors") // Maps this entity to the "doctors" table
@Getter // Lombok: generates getter methods
@Setter // Lombok: generates setter methods
@NoArgsConstructor // Lombok: generates a no-argument constructor
@AllArgsConstructor // Lombok: generates an all-argument constructor
@ToString // Lombok: generates a toString method
@EqualsAndHashCode // Lombok: generates equals and hashCode methods
public class Doctor {
    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates the ID value using identity strategy
    private Long id;

    @Column(nullable = false) // Maps to a non-null column in the database
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    @Email // Ensures the value is a valid email address
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "doctor_id") // Foreign key column for available dates
    @ToString.Exclude // Exclude from toString to prevent recursion
    @EqualsAndHashCode.Exclude // Exclude from equals/hashCode to prevent recursion
    private List<AvailableDate> availableDates = new ArrayList<>();

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    // One doctor can have many appointments; mapped by the "doctor" field in Appointment
    @ToString.Exclude // Exclude from toString to prevent recursion
    @EqualsAndHashCode.Exclude // Exclude from equals/hashCode to prevent recursion
    private List<Appointment> appointmentList = new ArrayList<>();
}
