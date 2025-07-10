package dev.patika.vet_management_system.business.concretes;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.patika.vet_management_system.business.abstracts.IVaccineService;
import dev.patika.vet_management_system.core.utils.Message;
import dev.patika.vet_management_system.dao.AnimalRepo;
import dev.patika.vet_management_system.dao.VaccineRepo;
import dev.patika.vet_management_system.dto.response.AnimalVaccineExpiringResponse;
import dev.patika.vet_management_system.entities.Animal;
import dev.patika.vet_management_system.entities.Vaccine;

@Service // Marks this class as a Spring service component
public class VaccineManager implements IVaccineService {

    // Repository for vaccine data access
    private final VaccineRepo vaccineRepo;
    // Repository for animal data access
    private final AnimalRepo animalRepo;

    // Constructor-based dependency injection for repositories
    public VaccineManager(VaccineRepo vaccineRepo, AnimalRepo animalRepo) {
        this.vaccineRepo = vaccineRepo;
        this.animalRepo = animalRepo;
    }

    @Override
    @Transactional // Ensures atomicity for save operations
    public Vaccine save(Vaccine vaccine) {
        // Allow saving only if expiration date (protection finish date) is more than
        // six months away
        LocalDate sixMonthsLeft = vaccine.getProtectionFinishDate().minusMonths(6);
        if (!LocalDate.now().isAfter(sixMonthsLeft)) {
            throw new IllegalArgumentException(
                    "Vaccine can only be registered if expiration date (protection finish date) is more than six months away.");
        }
        // Ensure we have a valid animal
        if (vaccine.getAnimal() != null && vaccine.getAnimal().getId() != null) {
            Animal animal = animalRepo.findById(vaccine.getAnimal().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Animal not found"));
            // Set the animal and establish bidirectional relationship
            vaccine.setAnimal(animal);
            animal.addVaccine(vaccine);
        }
        // Clear the ID to ensure a new record is created
        vaccine.setId(null);
        // Save the vaccine entity and return the result
        return this.vaccineRepo.save(vaccine);
    }

    @Override
    public Vaccine get(Long id) {
        // Retrieve the vaccine by ID or throw an exception if not found
        return this.vaccineRepo.findById((long) id)
                .orElseThrow(() -> new IllegalArgumentException(Message.NOT_FOUND));
    }

    @Override
    @Transactional // Ensures atomicity for update operations
    public Vaccine update(Vaccine vaccine) {
        // Fetch the existing managed entity
        Vaccine existingVaccine = this.vaccineRepo.findById(vaccine.getId())
                .orElseThrow(() -> new IllegalArgumentException("Vaccine not found"));

        // Update fields
        existingVaccine.setName(vaccine.getName());
        existingVaccine.setCode(vaccine.getCode());
        existingVaccine.setProtectionStartDate(vaccine.getProtectionStartDate());
        existingVaccine.setProtectionFinishDate(vaccine.getProtectionFinishDate());

        // Set the animal (fetch managed reference)
        if (vaccine.getAnimal() != null && vaccine.getAnimal().getId() != null) {
            Animal animal = animalRepo.findById(vaccine.getAnimal().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Animal not found"));
            existingVaccine.setAnimal(animal);
        }

        // Save and return the managed entity
        return this.vaccineRepo.save(existingVaccine);
    }

    @Override
    public boolean delete(Long id) {
        // Ensure the vaccine exists before deleting
        this.get(id);
        // Delete the vaccine by ID
        this.vaccineRepo.deleteById(id);
        return true;
    }

    // Returns animals with at least one vaccine expiring in 2 weeks
    public List<AnimalVaccineExpiringResponse> getAnimalsWithVaccineExpiringInTwoWeeks() {
        LocalDate today = LocalDate.now();
        LocalDate twoWeeksLater = today.plusWeeks(2);
        List<Vaccine> expiringVaccines = vaccineRepo.findByProtectionFinishDateBetween(today, twoWeeksLater);

        return expiringVaccines.stream()
                .map(vaccine -> {
                    Animal animal = vaccine.getAnimal();
                    return new AnimalVaccineExpiringResponse(
                            animal.getId(),
                            animal.getName(),
                            animal.getSpecies(),
                            animal.getBreed(),
                            animal.getGender(),
                            animal.getColour(),
                            animal.getDateOfBirth().toString(),
                            animal.getCustomer().getId(),
                            vaccine.getCode(),
                            vaccine.getProtectionFinishDate().toString()
                    );
                })
                .toList();
    }

}
