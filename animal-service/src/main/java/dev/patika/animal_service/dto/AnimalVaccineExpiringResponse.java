package dev.patika.animal_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalVaccineExpiringResponse {
    private Long id;
    private String name;
    private String species;
    private String breed;
    private String gender;
    private String colour;
    private String dateOfBirth;
    private Long customerId;
    private String vaccineCode;
    private String protectionFinishDate;
}