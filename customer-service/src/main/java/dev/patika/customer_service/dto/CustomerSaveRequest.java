package dev.patika.customer_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CustomerSaveRequest {

    @NotNull(message = "Name cannot be null")
    private String name;

    @Email
    @NotNull(message = "Email cannot be null")
    private String email;

    @NotNull(message = "Phone cannot be null")
    private String phone;

    private String address;
    
    private String city;

}
