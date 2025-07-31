package dev.patika.doctor_service.service;

import dev.patika.doctor_service.entities.Doctor;

public interface IDoctorService {

    Doctor save(Doctor doctor);

    Doctor get(Long id);

    Doctor update(Doctor doctor);

    boolean delete(Long id);
}
