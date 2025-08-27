package dev.patika.vet_management_system.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "vaccines")
@Data
public class Vaccine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private LocalDate protectionStartDate;

    @Column(nullable = false)
    private LocalDate protectionFinishDate;

    public boolean isRegistrationAllowed() {
        LocalDate sixMonthsLater = LocalDate.now().plusMonths(6);
        return this.protectionFinishDate.isAfter(sixMonthsLater);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Animal animal;
}
}
