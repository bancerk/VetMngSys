package dev.patika.animal_service.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "animals")
@Data
@ToString
@EqualsAndHashCode
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
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
    @Past
    private LocalDate dateOfBirth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference
    private Customer customer;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Vaccine> vaccineList;

    @OneToMany(mappedBy = "animal", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Appointment> appointmentList;

    @PrePersist
    @PreUpdate
    private void validateAndInitialize() {
        if (vaccineList == null) {
            vaccineList = new ArrayList<>();
        }
        if (appointmentList == null) {
            appointmentList = new ArrayList<>();
        }
    }

    public void addVaccine(Vaccine vaccine) {
        if (vaccineList == null) {
            vaccineList = new ArrayList<>();
        }
        vaccineList.add(vaccine);
        vaccine.setAnimal(this);
    }

    public void removeVaccine(Vaccine vaccine) {
        if (vaccineList != null) {
            vaccineList.remove(vaccine);
            vaccine.setAnimal(null);
        }
    }
}
