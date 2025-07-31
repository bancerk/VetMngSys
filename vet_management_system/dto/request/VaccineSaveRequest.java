package dev.patika.vet_management_system.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccineSaveRequest {

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Code cannot be null")
    private String code;

    @FutureOrPresent(message = "Protection start date must be in the present or future")
    @NotNull(message = "Protection start date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate protectionStartDate;

    @Future(message = "Protection finish date must be in the future")
    @NotNull(message = "Protection finish date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate protectionFinishDate;

    @NotNull(message = "Animal ID cannot be null")
    Long animalId;
}
