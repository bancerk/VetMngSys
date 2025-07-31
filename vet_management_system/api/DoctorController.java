package dev.patika.vet_management_system.api;

// Import necessary classes for controller functionality, data transfer, validation, and HTTP handling
import dev.patika.vet_management_system.business.concretes.DoctorManager;
import dev.patika.vet_management_system.core.modelMapper.ModelMapperService;
import dev.patika.vet_management_system.core.utils.Result;
import dev.patika.vet_management_system.core.utils.ResultData;
import dev.patika.vet_management_system.core.utils.ResultHelper;
import dev.patika.vet_management_system.dto.request.DoctorSaveRequest;
import dev.patika.vet_management_system.dto.request.DoctorUpdateRequest;
import dev.patika.vet_management_system.dto.response.DoctorResponse;
import dev.patika.vet_management_system.entities.Doctor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// Mark this class as a REST controller to handle HTTP requests
@RestController
// Set the base URL path for all endpoints in this controller
@RequestMapping("/doctor")
public class DoctorController {
    
    // Service for doctor-related business logic
    private final DoctorManager doctorService;
    // Service for mapping between DTOs and entities
    private final ModelMapperService modelMapper;

    // Constructor-based dependency injection for services
    public DoctorController(DoctorManager doctorService, ModelMapperService modelMapper) {
        this.doctorService = doctorService;
        this.modelMapper = modelMapper;
    }

    // Endpoint to create a new doctor
    // Accepts a POST request at /doctor/save
    // Expects a valid DoctorSaveRequest in the request body
    // Returns the created doctor data with HTTP 201 status
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<DoctorResponse> save(@Valid @RequestBody DoctorSaveRequest doctorSaveRequest) {
        // Map the incoming DTO to a Doctor entity
        Doctor saveDoctor = this.modelMapper.forRequest().map(doctorSaveRequest, Doctor.class);
        // Save the doctor using the service
        this.doctorService.save(saveDoctor);
        // Map the saved entity to a response DTO
        DoctorResponse doctorResponse = this.modelMapper.forResponse().map(saveDoctor, DoctorResponse.class);
        // Return a standardized response
        return ResultHelper.created(doctorResponse);
    }

    // Endpoint to retrieve a doctor by ID
    // Accepts a GET request at /doctor/{id}
    // Returns the doctor data with HTTP 200 status
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<DoctorResponse> findById(@PathVariable Long id) {
        // Retrieve the doctor entity by ID
        Doctor doctor = this.doctorService.get(id);
        // Map the entity to a response DTO
        DoctorResponse doctorResponse = this.modelMapper.forResponse().map(doctor, DoctorResponse.class);
        // Return a standardized response
        return ResultHelper.successData(doctorResponse);
    }

    // Endpoint to update an existing doctor
    // Accepts a PUT request at /doctor/update
    // Expects a valid DoctorUpdateRequest in the request body
    // Returns the updated doctor data with HTTP 200 status
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<DoctorResponse> update(@Valid @RequestBody DoctorUpdateRequest doctorUpdateRequest) {
        // Map the incoming DTO to a Doctor entity
        Doctor updateDoctor = this.modelMapper.forRequest().map(doctorUpdateRequest, Doctor.class);
        // Update the doctor using the service
        this.doctorService.update(updateDoctor);
        // Map the updated entity to a response DTO
        DoctorResponse doctorResponse = this.modelMapper.forResponse().map(updateDoctor, DoctorResponse.class);
        // Return a standardized response
        return ResultHelper.successData(doctorResponse);
    }

    // Endpoint to delete a doctor by ID
    // Accepts a DELETE request at /doctor/delete/{id}
    // Returns a success response with HTTP 200 status
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        // Delete the doctor using the service
        this.doctorService.delete(id);
        // Return a standardized success response
        return ResultHelper.success();
    }
}
