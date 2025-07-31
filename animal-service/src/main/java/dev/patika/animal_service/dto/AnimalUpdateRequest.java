package dev.patika.animal_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnimalUpdateRequest {

    @Positive(message = "Id must be a positive number")
    @NotNull(message = "Id cannot be null")
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Date of birth must be in the past or present")
    private LocalDate dateOfBirth;

    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;
}
