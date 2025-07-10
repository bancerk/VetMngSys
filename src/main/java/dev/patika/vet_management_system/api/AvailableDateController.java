package dev.patika.vet_management_system.api;

import dev.patika.vet_management_system.business.concretes.AvailableDateManager;
import dev.patika.vet_management_system.business.concretes.DoctorManager;
import dev.patika.vet_management_system.core.modelMapper.ModelMapperService;
import dev.patika.vet_management_system.core.utils.Result;
import dev.patika.vet_management_system.core.utils.ResultData;
import dev.patika.vet_management_system.core.utils.ResultHelper;
import dev.patika.vet_management_system.dto.request.AvailableDateSaveRequest;
import dev.patika.vet_management_system.dto.request.AvailableDateUpdateRequest;
import dev.patika.vet_management_system.dto.response.AvailableDateResponse;
import dev.patika.vet_management_system.entities.AvailableDate;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController // Marks this class as a REST controller
@RequestMapping("/availabledate") // Base URL mapping for all endpoints in this controller
public class AvailableDateController {

    // Service dependencies injected via constructor
    private final AvailableDateManager availableDateService;
    private final ModelMapperService modelMapper;

    // Constructor for dependency injection
    public AvailableDateController(
            AvailableDateManager availableDateService,
            DoctorManager doctorService,
            ModelMapperService modelMapper) {
        this.availableDateService = availableDateService;
        this.modelMapper = modelMapper;
    }

    // Endpoint to save a new available date
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED) // Returns HTTP 201 on success
    public ResultData<AvailableDateResponse> save(@Valid @RequestBody AvailableDateSaveRequest request) {
        try {
            // Create new AvailableDate instance and set its fields from the request
            AvailableDate availableDate = new AvailableDate();
            availableDate.setAvailableDate(request.getAvailableDate());
            availableDate.setDoctorId(request.getDoctorId());

            // Save the available date and get the saved instance
            AvailableDate savedDate = this.availableDateService.save(availableDate);

            // Map the saved entity to the response DTO
            AvailableDateResponse response = this.modelMapper.forResponse().map(savedDate, AvailableDateResponse.class);
            return ResultHelper.created(response); // Return a created response
        } catch (Exception e) {
            // Handle any exceptions and throw an IllegalArgumentException with a message
            throw new IllegalArgumentException("Error saving available date: " + e.getMessage());
        }
    }

    // Endpoint to get an available date by its ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK) // Returns HTTP 200 on success
    public ResultData<AvailableDateResponse> findById(@PathVariable Long id) {
        AvailableDate availableDate = this.availableDateService.get(id); // Retrieve available date by ID
        AvailableDateResponse availableDateResponse = this.modelMapper.forResponse().map(availableDate,
                AvailableDateResponse.class); // Map to response DTO
        return ResultHelper.successData(availableDateResponse); // Return success response
    }

    // Endpoint to update an existing available date
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK) // Returns HTTP 200 on success
    public ResultData<AvailableDateResponse> update(
            @Valid @RequestBody AvailableDateUpdateRequest availableDateUpdateRequest) {
        // Map the update request to the entity
        AvailableDate updateAvailableDate = this.modelMapper.forRequest().map(availableDateUpdateRequest,
                AvailableDate.class);
        this.availableDateService.update(updateAvailableDate); // Update the available date
        AvailableDateResponse availableDateResponse = this.modelMapper.forResponse().map(updateAvailableDate,
                AvailableDateResponse.class); // Map to response DTO
        return ResultHelper.successData(availableDateResponse); // Return success response
    }

    // Endpoint to delete an available date by its ID
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK) // Returns HTTP 200 on success
    public Result delete(@PathVariable("id") Long id) {
        this.availableDateService.delete(id); // Delete the available date
        return ResultHelper.success(); // Return success response
    }
}
