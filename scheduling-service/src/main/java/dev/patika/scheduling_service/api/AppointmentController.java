package dev.patika.scheduling_service.api;

import dev.patika.scheduling_service.service.IAppointmentService;
import dev.patika.scheduling_service.dto.AppointmentSaveRequest;
import dev.patika.scheduling_service.dto.AppointmentResponse;
import dev.patika.scheduling_service.entities.Appointment;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {

    private final IAppointmentService appointmentService;
    private final IModelMapperService modelMapper;

    public AppointmentController(IAppointmentService appointmentService, IModelMapperService modelMapper) {
        this.appointmentService = appointmentService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AppointmentResponse> save(@Valid @RequestBody AppointmentSaveRequest appointmentSaveRequest) {
        // You'll need to create the DTOs and configure ModelMapper for this service
        Appointment newAppointment = this.modelMapper.forRequest().map(appointmentSaveRequest, Appointment.class);

        // The business logic, including the call to animal-service, is in the manager
        this.appointmentService.save(newAppointment);

        AppointmentResponse appointmentResponse = this.modelMapper.forResponse().map(newAppointment,
                AppointmentResponse.class);
        return ResultHelper.created(appointmentResponse);
    }

    // ... other endpoints for GET, PUT, DELETE, and filtering
}
