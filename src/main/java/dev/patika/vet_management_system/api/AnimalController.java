package dev.patika.vet_management_system.api;

import dev.patika.vet_management_system.core.modelMapper.ModelMapperService;
import dev.patika.service.CustomerManager;
import dev.patika.vet_management_system.business.concretes.AnimalManager;
import dev.patika.vet_management_system.core.utils.Result;
import dev.patika.vet_management_system.core.utils.ResultData;
import dev.patika.vet_management_system.core.utils.ResultHelper;
import dev.patika.vet_management_system.dto.request.AnimalSaveRequest;
import dev.patika.vet_management_system.dto.request.AnimalUpdateRequest;
import dev.patika.vet_management_system.dto.response.AnimalResponse;
import dev.patika.vet_management_system.dto.response.VaccineResponse;
import dev.patika.vet_management_system.entities.Animal;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController // Marks this class as a REST controller
@RequestMapping("/animal") // Base URL mapping for all endpoints in this controller
public class AnimalController {

    // Service dependencies injected via constructor
    private final AnimalManager animalService;
    private final CustomerManager customerService;
    private final ModelMapperService modelMapper;

    // Constructor for dependency injection
    public AnimalController(AnimalManager animalService, CustomerManager customerService,
            ModelMapperService modelMapper) {
        this.animalService = animalService;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    // Endpoint to save a new animal
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED) // Returns HTTP 201 on success
    public ResultData<AnimalResponse> save(@Valid @RequestBody AnimalSaveRequest animalSaveRequest) {
        try {
            // Map the request DTO to the Animal entity
            Animal saveAnimal = this.modelMapper.forRequest().map(animalSaveRequest, Animal.class);

            // Set the customer for the animal using the provided customer ID from the request
            saveAnimal.setCustomer(customerService.get(animalSaveRequest.getCustomerId()));

            // Save the animal entity and get the saved instance
            Animal savedAnimal = this.animalService.save(saveAnimal);

            // Map the saved entity to the response DTO
            AnimalResponse animalResponse = this.modelMapper.forResponse().map(savedAnimal, AnimalResponse.class);
            return ResultHelper.created(animalResponse); // Return a created response
        } catch (Exception e) {
            // Handle any exceptions and throw an IllegalArgumentException with a message
            throw new IllegalArgumentException("Error saving animal: " + e.getMessage());
        }
    }

    // Endpoint to get an animal by its ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK) // Returns HTTP 200 on success
    public ResultData<AnimalResponse> findById(@PathVariable Long id) {
        Animal animal = this.animalService.get(id); // Retrieve animal by ID
        AnimalResponse animalResponse = this.modelMapper.forResponse().map(animal, AnimalResponse.class); // Map to response DTO
        return ResultHelper.successData(animalResponse); // Return success response
    }

    // Endpoint to update an existing animal
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK) // Returns HTTP 200 on success
    public ResultData<AnimalResponse> update(@Valid @RequestBody AnimalUpdateRequest animalUpdateRequest) {
        Animal updateAnimal = this.modelMapper.forRequest().map(animalUpdateRequest, Animal.class); // Map request to entity
        this.animalService.update(updateAnimal); // Update the animal
        AnimalResponse animalResponse = this.modelMapper.forResponse().map(updateAnimal, AnimalResponse.class); // Map to response DTO
        return ResultHelper.successData(animalResponse); // Return success response
    }

    // Endpoint to delete an animal by its ID
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK) // Returns HTTP 200 on success
    public Result delete(@PathVariable("id") Long id) {
        this.animalService.delete(id); // Delete the animal
        return ResultHelper.success(); // Return success response
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK) // Returns HTTP 200 on success
    public ResultData<AnimalResponse> findByName(@RequestParam String name) {
        Optional<Animal> animal = this.animalService.getName(name); // Retrieve animal by name
        AnimalResponse animalResponse = this.modelMapper.forResponse().map(animal.get(), AnimalResponse.class); // Map to response DTO
        return ResultHelper.successData(animalResponse); // Return success response
    }

    // Endpoint to get all vaccines for an animal by its name
    // Accepts a GET request at /animal/vaccines?name={animalName}
    // Returns a list of VaccineResponse objects with HTTP 200 status
    // getVaccinesByAnimal
    @GetMapping("/vaccines")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> getVaccinesByAnimalName(@RequestParam String name) {
        Optional<Animal> foundAnimal = animalService.getName(name);

        if (foundAnimal.isPresent()) {
            List<VaccineResponse> vaccineResponses = foundAnimal.get().getVaccineList().stream()
                    .map(vaccine -> this.modelMapper.forResponse().map(vaccine, VaccineResponse.class))
                    .toList();
            return ResultHelper.successData(vaccineResponses);
        } else {
            return ResultHelper.successData(List.of());
        }
    }

}
