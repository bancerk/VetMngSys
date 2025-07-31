package dev.patika.doctor_service.service;

import dev.patika.doctor_service.service.IDoctorService;
import dev.patika.doctor_service.repo.DoctorRepo;
import dev.patika.doctor_service.entities.Doctor;
import org.springframework.stereotype.Service;

@Service
public class DoctorManager implements IDoctorService {

    private final DoctorRepo doctorRepo;

    public DoctorManager(DoctorRepo doctorRepo) {
        this.doctorRepo = doctorRepo;
    }

    @Override
    public Doctor save(Doctor doctor) {
        return this.doctorRepo.save(doctor);
    }

    @Override
    public Doctor get(Long id) {
        return this.doctorRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(Message.NOT_FOUND));
    }

    @Override
    public Doctor update(Doctor doctor) {
        this.get(doctor.getId());
        return this.doctorRepo.save(doctor);
    }

    @Override
    public boolean delete(Long id) {
        this.get(id);
        this.doctorRepo.deleteById(id);
        return true;
    }
}
