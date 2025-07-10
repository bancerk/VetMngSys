package dev.patika.vet_management_system.dto.request;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDateUpdateRequest {

    @NotNull(message = "ID cannot be null")
    @Positive(message = "ID must be positive")
    private Long id;

    @NotNull(message = "Available date cannot be null")
    @FutureOrPresent(message = "Available date must be in the present or future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate availableDate;

    @NotNull(message = "Doctor ID cannot be null")
    @Positive(message = "Doctor ID must be positive")
    private Long doctorId;
}
