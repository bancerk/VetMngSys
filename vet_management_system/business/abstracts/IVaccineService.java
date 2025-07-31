package dev.patika.vet_management_system.business.abstracts;

import dev.patika.vet_management_system.entities.Vaccine;

public interface IVaccineService {

    Vaccine save(Vaccine vaccine);

    Vaccine get(Long id);

    Vaccine update(Vaccine vaccine);

    boolean delete(Long id);
}