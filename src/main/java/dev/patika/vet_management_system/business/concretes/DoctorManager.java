package dev.patika.vet_management_system.business.concretes;

import dev.patika.vet_management_system.business.abstracts.IDoctorService;
import dev.patika.vet_management_system.core.exceptions.NotFoundException;
import dev.patika.vet_management_system.core.utils.Message;
import dev.patika.vet_management_system.dao.DoctorRepo;
import dev.patika.vet_management_system.entities.Doctor;
import org.springframework.stereotype.Service;

@Service // Marks this class as a Spring service component
public class DoctorManager implements IDoctorService {

    // Repository for doctor data access
    private final DoctorRepo doctorRepo;

    // Constructor-based dependency injection for repository
    public DoctorManager(DoctorRepo doctorRepo) {
        this.doctorRepo = doctorRepo;
    }

    @Override
    public Doctor save(Doctor doctor) {
        // Save the doctor entity and return the result
        return this.doctorRepo.save(doctor);
    }

    @Override
    public Doctor get(Long id) {
        // Retrieve the doctor by ID or throw NotFoundException if not found
        return this.doctorRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(Message.NOT_FOUND));
    }

    @Override
    public Doctor update(Doctor doctor) {
        // Ensure the doctor exists before updating
        this.get(doctor.getId()); // Ensure the doctor exists
        // Save and return the updated doctor entity
        return this.doctorRepo.save(doctor);
    }

    @Override
    public boolean delete(Long id) {
        // Ensure the doctor exists before deleting by calling get(id)
        this.get(id); // Will throw NotFoundException if not found
        // Delete the doctor by ID
        this.doctorRepo.deleteById(id);
        return true;
    }
}
