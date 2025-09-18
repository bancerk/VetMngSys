package dev.patika.scheduling_service.service;

import dev.patika.scheduling_service.entities.Appointment;

import java.util.List;

public interface IAppointmentService {

    Appointment save(Appointment appointment);

    List<Appointment> getAll();

    Appointment getById(Long id);

    Appointment update(Appointment appointment);

    void delete(Long id);
}
