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

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    private final AppointmentManager appointmentManager;
    private final ModelMapperService modelMapper;

    private final AnimalRepo animalRepo;
    private final DoctorRepo doctorRepo;

    public AppointmentController(AppointmentManager appointmentManager, ModelMapperService modelMapper,
            AnimalRepo animalRepo, DoctorRepo doctorRepo) {
        this.appointmentManager = appointmentManager;
        this.modelMapper = modelMapper;
        this.animalRepo = animalRepo;
        this.doctorRepo = doctorRepo;
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AppointmentResponse> save(@Valid @RequestBody AppointmentSaveRequest appointmentSaveRequest) {
        Appointment appointment = this.modelMapper.forRequest().map(appointmentSaveRequest, Appointment.class);

        Animal animal = animalRepo.findById(appointmentSaveRequest.getAnimalId())
                .orElse(null);
        Doctor doctor = doctorRepo.findById(appointmentSaveRequest.getDoctorId())
                .orElse(null);
        appointment.setAnimal(animal);
        appointment.setDoctor(doctor);

        Appointment savedAppointment = this.appointmentManager.save(appointment);

        AppointmentResponse response = this.modelMapper.forResponse().map(savedAppointment, AppointmentResponse.class);
        return ResultHelper.created(response);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse> findById(@PathVariable Long id) {
        Appointment appointment = this.appointmentManager.get(id);
        AppointmentResponse response = this.modelMapper.forResponse().map(appointment, AppointmentResponse.class);
        return ResultHelper.successData(response);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AppointmentResponse> update(
            @Valid @RequestBody AppointmentUpdateRequest appointmentUpdateRequest) {
        Appointment appointment = this.modelMapper.forRequest().map(appointmentUpdateRequest, Appointment.class);

        Animal animal = animalRepo.findById(appointmentUpdateRequest.getAnimalId()).orElse(null);
        Doctor doctor = doctorRepo.findById(appointmentUpdateRequest.getDoctorId()).orElse(null);
        appointment.setAnimal(animal);
        appointment.setDoctor(doctor);

        Appointment updatedAppointment = this.appointmentManager.update(appointment);

        AppointmentResponse response = this.modelMapper.forResponse().map(updatedAppointment,
                AppointmentResponse.class);

        return ResultHelper.successData(response);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        this.appointmentManager.delete(id);
        return ResultHelper.success();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AppointmentResponse>> findByAppointmentDate(
            @RequestParam("appointmentDate") LocalDateTime appointmentDate) {
        List<Appointment> appointments = this.appointmentManager.findByAppointmentDate(appointmentDate);
        List<AppointmentResponse> responses = appointments.stream()
                .map(a -> this.modelMapper.forResponse().map(a, AppointmentResponse.class)).toList();
        return ResultHelper.successData(responses);
    }

    @GetMapping(params = "animalName")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AppointmentResponse>> findByAnimalName(@RequestParam("animalName") String animalName) {
        List<Appointment> appointments = this.appointmentManager.findByAnimalName(animalName);
        List<AppointmentResponse> responses = appointments.stream()
                .map(a -> this.modelMapper.forResponse().map(a, AppointmentResponse.class)).toList();
        return ResultHelper.successData(responses);
    }

    @GetMapping(params = "doctorName")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AppointmentResponse>> findByDoctorName(@RequestParam("doctorName") String doctorName) {
        List<Appointment> appointments = this.appointmentManager.findByDoctorName(doctorName);
        List<AppointmentResponse> responses = appointments.stream()
                .map(a -> this.modelMapper.forResponse().map(a, AppointmentResponse.class)).toList();
        return ResultHelper.successData(responses);
    }
}
