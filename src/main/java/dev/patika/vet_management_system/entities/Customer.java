package dev.patika.vet_management_system.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity // Marks this class as a JPA entity
@Table(name = "customers") // Maps this entity to the "customers" table
@Getter // Lombok: generates getter methods
@Setter // Lombok: generates setter methods
@NoArgsConstructor // Lombok: generates a no-argument constructor
@AllArgsConstructor // Lombok: generates an all-argument constructor
public class Customer {

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

    @ToString.Exclude // Exclude from toString to prevent recursion
    @EqualsAndHashCode.Exclude // Exclude from equals/hashCode to prevent recursion
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Helps manage the bidirectional relationship with Animal
    // One customer can have many animals; mapped by the "customer" field in Animal
    private List<Animal> animalList;

    // Helper method to add an animal and set the bidirectional relationship
    public void addAnimal(Animal animal) {
        animalList.add(animal);
        animal.setCustomer(this);
    }

    // Helper method to remove an animal and clear the relationship
    public void removeAnimal(Animal animal) {
        animalList.remove(animal);
        animal.setCustomer(null);
    }
}
