package dev.patika.vet_management_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorUpdateRequest {

    @Positive(message = "Id must be a positive number")
    @NotNull(message = "Id cannot be null")
    private Long id;

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Phone cannot be null")
    private String phone;

    @Email
    @NotNull(message = "Email cannot be null")
    private String email;

    private String address;

    private String city;

}
