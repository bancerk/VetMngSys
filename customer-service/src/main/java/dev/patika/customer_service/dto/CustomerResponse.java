package dev.patika.customer_service.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CustomerResponse {

    @Id
    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotNull(message = "Name cannot be null")
    private String name;

    @Email(message = "Email must be a valid email address")
    @NotNull(message = "Email cannot be null")
    private String email;

    @NotNull(message = "Phone cannot be null")
    private String phone;

    @NotNull(message = "Address cannot be null")
    private String address;

    @NotNull(message = "City cannot be null")
    private String city;

}
