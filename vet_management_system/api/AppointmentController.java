package dev.patika.vet_management_system.api;

import dev.patika.vet_management_system.business.concretes.AppointmentManager;
import dev.patika.vet_management_system.core.modelMapper.ModelMapperService;
import dev.patika.vet_management_system.core.utils.Result;
import dev.patika.vet_management_system.core.utils.ResultData;
import dev.patika.vet_management_system.core.utils.ResultHelper;
import dev.patika.vet_management_system.dao.AnimalRepo;
import dev.patika.vet_management_system.dao.DoctorRepo;
import dev.patika.vet_management_system.dto.request.AppointmentSaveRequest;
import dev.patika.vet_management_system.dto.request.AppointmentUpdateRequest;
import dev.patika.vet_management_system.dto.response.AppointmentResponse;
import dev.patika.vet_management_system.entities.Appointment;
import dev.patika.vet_management_system.entities.Animal;
import dev.patika.vet_management_system.entities.Doctor;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController // Marks this class as a REST controller
@RequestMapping("/appointment") // Base URL mapping for all endpoints in this controller
// This class serves as a REST controller for managing appointments in the vet
// management system.
public class AppointmentController {

    // Service for appointment-related business logic
    private final AppointmentManager appointmentManager;
    // Service for mapping between DTOs and entities
    private final ModelMapperService modelMapper;

    private final AnimalRepo animalRepo;
    private final DoctorRepo doctorRepo;

    // Constructor-based dependency injection for services
    public AppointmentController(AppointmentManager appointmentManager, ModelMapperService modelMapper,
            AnimalRepo animalRepo, DoctorRepo doctorRepo) {
        this.appointmentManager = appointmentManager;
        this.modelMapper = modelMapper;
        this.animalRepo = animalRepo;
        this.doctorRepo = doctorRepo;
    }

    // Endpoint to create a new appointment
    // Accepts a POST request at /appointment/save
    // Expects a valid AppointmentSaveRequest in the request body
    // Returns the created appointment data with HTTP 201 status
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AppointmentResponse> save(@Valid @RequestBody AppointmentSaveRequest appointmentSaveRequest) {
        // Map the incoming DTO to an Appointment entity
        Appointment appointment = this.modelMapper.forRequest().map(appointmentSaveRequest, Appointment.class);

        // Set the full Animal and Doctor entities
        Animal animal = animalRepo.findById(appointmentSaveRequest.getAnimalId())
                .orElse(null);
        Doctor doctor = doctorRepo.findById(appointmentSaveRequest.getDoctorId())
                .orElse(null);
        appointment.setAnimal(animal);
        appointment.setDoctor(doctor);

        // Save the appointment using the service
        Appointment savedAppointment = this.appointmentManager.save(appointment);

        // Map the saved entity to a response DTO
        AppointmentResponse response = this.modelMapper.forResponse().map(savedAppointment, AppointmentResponse.class);
        // Return a standardized response
        return ResultHelper.created(response);
    }

    // Endpoint to retrieve an appointment by ID
    // Accepts a GET request at /appointment/{id}
    // Returns the appointment data with HTTP 200 status
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse> findById(@PathVariable Long id) {
        // Retrieve the appointment entity by ID
        Appointment appointment = this.appointmentManager.get(id);
        // Map the entity to a response DTO
        AppointmentResponse response = this.modelMapper.forResponse().map(appointment, AppointmentResponse.class);
        // Return a standardized response
        return ResultHelper.successData(response);
    }

    // Endpoint to update an existing appointment
    // Accepts a PUT request at /appointment/update
    // Expects a valid AppointmentUpdateRequest in the request body
    // Returns the updated appointment data with HTTP 200 status
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse> update(
            @Valid @RequestBody AppointmentUpdateRequest appointmentUpdateRequest) {
        // Map the incoming DTO to an Appointment entity
        Appointment appointment = this.modelMapper.forRequest().map(appointmentUpdateRequest, Appointment.class);

        // Set the full Animal and Doctor entities
        Animal animal = animalRepo.findById(appointmentUpdateRequest.getAnimalId()).orElse(null);
        Doctor doctor = doctorRepo.findById(appointmentUpdateRequest.getDoctorId()).orElse(null);
        appointment.setAnimal(animal);
        appointment.setDoctor(doctor);

        // Update the appointment using the service
        Appointment updatedAppointment = this.appointmentManager.update(appointment);

        // Map the updated entity to a response DTO
        AppointmentResponse response = this.modelMapper.forResponse().map(updatedAppointment,
                AppointmentResponse.class);

        // Return a standardized response
        return ResultHelper.successData(response);
    }

    // Endpoint to delete an appointment by ID
    // Accepts a DELETE request at /appointment/delete/{id}
    // Returns a success response with HTTP 200 status
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        // Delete the appointment using the service
        this.appointmentManager.delete(id);
        // Return a standardized success response
        return ResultHelper.success();
    }

    // Endpoint to retrieve appointments by date
    // Accepts a GET request at /appointment
    // Expects a request parameter "appointmentDate" of type LocalDateTime
    // Returns a list of appointments on the specified date with HTTP 200 status
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AppointmentResponse>> findByAppointmentDate(
            @RequestParam("appointmentDate") LocalDateTime appointmentDate) {
        List<Appointment> appointments = this.appointmentManager.findByAppointmentDate(appointmentDate);
        List<AppointmentResponse> responses = appointments.stream()
                .map(a -> this.modelMapper.forResponse().map(a, AppointmentResponse.class)).toList();
        return ResultHelper.successData(responses);
    }

    // Endpoint to retrieve appointments by animal name
    // Accepts a GET request at /appointment?animalName={animalName}
    // Expects a request parameter "animalName"
    // This endpoint allows filtering appointments by the name of the animal
    // Returns a list of appointments for the specified animal with HTTP 200 status
    @GetMapping(params = "animalName")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AppointmentResponse>> findByAnimalName(@RequestParam("animalName") String animalName) {
        List<Appointment> appointments = this.appointmentManager.findByAnimalName(animalName);
        List<AppointmentResponse> responses = appointments.stream()
                .map(a -> this.modelMapper.forResponse().map(a, AppointmentResponse.class)).toList();
        return ResultHelper.successData(responses);
    }

    // Endpoint to retrieve appointments by doctor name
    // Accepts a GET request at /appointment?doctorName={doctorName}
    // Returns a list of appointments for the specified doctor with HTTP 200 status
    @GetMapping(params = "doctorName")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AppointmentResponse>> findByDoctorName(@RequestParam("doctorName") String doctorName) {
        List<Appointment> appointments = this.appointmentManager.findByDoctorName(doctorName);
        List<AppointmentResponse> responses = appointments.stream()
                .map(a -> this.modelMapper.forResponse().map(a, AppointmentResponse.class)).toList();
        return ResultHelper.successData(responses);
    }
}
