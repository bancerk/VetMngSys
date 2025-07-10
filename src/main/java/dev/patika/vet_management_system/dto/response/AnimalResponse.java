package dev.patika.vet_management_system.dto.response;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalResponse {

    @Id
    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Species cannot be null")
    private String species;

    @NotNull(message = "Breed cannot be null")
    private String breed;

    @NotNull(message = "Gender cannot be null")
    private String gender;

    @NotNull(message = "Colour cannot be null")
    private String colour;

    @NotNull(message = "Date of birth cannot be null")
    private String dateOfBirth;

    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

}
