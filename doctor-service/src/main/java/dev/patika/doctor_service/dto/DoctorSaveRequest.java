package dev.patika.vet_management_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class DoctorSaveRequest {

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
