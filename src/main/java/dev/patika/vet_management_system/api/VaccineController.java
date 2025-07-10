package dev.patika.vet_management_system.api;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.patika.vet_management_system.business.concretes.AnimalManager;
import dev.patika.vet_management_system.business.concretes.VaccineManager;
import dev.patika.vet_management_system.core.modelMapper.ModelMapperService;
import dev.patika.vet_management_system.core.utils.Result;
import dev.patika.vet_management_system.core.utils.ResultData;
import dev.patika.vet_management_system.core.utils.ResultHelper;
import dev.patika.vet_management_system.dto.request.VaccineSaveRequest;
import dev.patika.vet_management_system.dto.request.VaccineUpdateRequest;
import dev.patika.vet_management_system.dto.response.AnimalVaccineExpiringResponse;
import dev.patika.vet_management_system.dto.response.VaccineResponse;
import dev.patika.vet_management_system.entities.Animal;
import dev.patika.vet_management_system.entities.Vaccine;
import jakarta.validation.Valid;

// Mark this class as a REST controller to handle HTTP requests
@RestController
// Set the base URL path for all endpoints in this controller
@RequestMapping("/vaccine")
public class VaccineController {

    // Service for vaccine-related business logic
    private final VaccineManager vaccineService;
    // Service for animal-related business logic
    private final AnimalManager animalService;
    // Service for mapping between DTOs and entities
    private final ModelMapperService modelMapper;

    // Constructor-based dependency injection for services
    public VaccineController(VaccineManager vaccineService, AnimalManager animalService,
            ModelMapperService modelMapper) {
        this.vaccineService = vaccineService;
        this.animalService = animalService;
        this.modelMapper = modelMapper;
    }

    // Endpoint to create a new vaccine
    // Accepts a POST request at /vaccine/save
    // Expects a valid VaccineSaveRequest in the request body
    // Returns the created vaccine data with HTTP 201 status
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<VaccineResponse> save(@Valid @RequestBody VaccineSaveRequest vaccineSaveRequest) {
        try {
            // Map the request to entity
            Vaccine saveVaccine = this.modelMapper.forRequest().map(vaccineSaveRequest, Vaccine.class);

            // Get the animal and set it for the vaccine
            Animal animal = animalService.get(vaccineSaveRequest.getAnimalId());
            saveVaccine.setAnimal(animal);

            // Save and get the result
            Vaccine savedVaccine = this.vaccineService.save(saveVaccine);

            // Map to response DTO
            VaccineResponse vaccineResponse = this.modelMapper.forResponse().map(savedVaccine, VaccineResponse.class);
            // When mapping Vaccine to VaccineResponse, set registrationAllowed
            vaccineResponse.setRegistrationAllowed(savedVaccine.isRegistrationAllowed());
            return ResultHelper.created(vaccineResponse);
        } catch (Exception e) {
            // Handle any exceptions and throw an IllegalArgumentException with a message
            throw new IllegalArgumentException("Error saving vaccine: " + e.getMessage());
        }
    }

    // Endpoint to retrieve a vaccine by ID
    // Accepts a GET request at /vaccine/{id}
    // Returns the vaccine data with HTTP 200 status
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<VaccineResponse> findById(@PathVariable Long id) {
        Vaccine vaccine = this.vaccineService.get(id);
        VaccineResponse vaccineResponse = this.modelMapper.forResponse().map(vaccine, VaccineResponse.class);
        return ResultHelper.successData(vaccineResponse);
    }

    // Endpoint to update an existing vaccine
    // Accepts a PUT request at /vaccine/update
    // Expects a valid VaccineUpdateRequest in the request body
    // Returns the updated vaccine data with HTTP 200 status
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<VaccineResponse> update(@Valid @RequestBody VaccineUpdateRequest vaccineUpdateRequest) {
        try {
            // Verify vaccine exists (will throw exception if not found)
            vaccineService.get(vaccineUpdateRequest.getId());

            // Map the request to entity but preserve the animal
            Vaccine updateVaccine = this.modelMapper.forRequest().map(vaccineUpdateRequest, Vaccine.class);

            // Get the animal and set it for the vaccine
            Animal animal = animalService.get(vaccineUpdateRequest.getAnimalId());
            updateVaccine.setAnimal(animal);

            // Update and get the result
            Vaccine updatedVaccine = this.vaccineService.update(updateVaccine);

            // Map to response DTO
            VaccineResponse vaccineResponse = this.modelMapper.forResponse().map(updatedVaccine, VaccineResponse.class);
            return ResultHelper.successData(vaccineResponse);
        } catch (Exception e) {
            // Handle any exceptions and throw an IllegalArgumentException with a message
            throw new IllegalArgumentException("Error updating vaccine: " + e.getMessage());
        }
    }

    // Endpoint to delete a vaccine by ID
    // Accepts a DELETE request at /vaccine/delete/{id}
    // Returns a success response with HTTP 200 status
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        this.vaccineService.delete(id);
        return ResultHelper.success();
    }

    // Endpoint to list animals with a vaccine expiring in 2 weeks
    // GET /vaccine/expiring-in-two-weeks
    @GetMapping("/expiring-in-two-weeks")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalVaccineExpiringResponse>> getAnimalsWithVaccineExpiringInTwoWeeks() {
        List<AnimalVaccineExpiringResponse> responses = vaccineService.getAnimalsWithVaccineExpiringInTwoWeeks();
        return ResultHelper.successData(responses);
    }

    // Endpoint to list all vaccines filtered by animal name
    @GetMapping("/animal")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> getVaccinesByAnimalName(@RequestParam("name") String animalName) {
        Optional<Animal> foundAnimalName = animalService.getName(animalName);

        if (foundAnimalName.isPresent()) {
            List<VaccineResponse> vaccineResponses = foundAnimalName.get().getVaccineList().stream()
                    .map(vaccine -> this.modelMapper.forResponse().map(vaccine, VaccineResponse.class))
                    .toList();
            return ResultHelper.successData(vaccineResponses);
        } else {
            throw new IllegalArgumentException("Animal not found with name: " + animalName);
        }
    }
}