package dev.patika.vet_management_system.business.abstracts;

import dev.patika.vet_management_system.entities.Doctor;

public interface IDoctorService {

    Doctor save(Doctor doctor);

    Doctor get(Long id);

    Doctor update(Doctor doctor);

    boolean delete(Long id);
}
