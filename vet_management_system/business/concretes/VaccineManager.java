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

@Service
public class VaccineManager implements IVaccineService {
    private final VaccineRepo vaccineRepo;
    private final AnimalRepo animalRepo;
    public VaccineManager(VaccineRepo vaccineRepo, AnimalRepo animalRepo) {
        this.vaccineRepo = vaccineRepo;
        this.animalRepo = animalRepo;
    }

    @Override
    @Transactional
    public Vaccine save(Vaccine vaccine) {
        LocalDate sixMonthsLeft = vaccine.getProtectionFinishDate().minusMonths(6);
        if (!LocalDate.now().isAfter(sixMonthsLeft)) {
            throw new IllegalArgumentException(
                    "Vaccine can only be registered if expiration date (protection finish date) is more than six months away.");
        }
        if (vaccine.getAnimal() != null && vaccine.getAnimal().getId() != null) {
            Animal animal = animalRepo.findById(vaccine.getAnimal().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Animal not found"));
            vaccine.setAnimal(animal);
            animal.addVaccine(vaccine);
        }
        vaccine.setId(null);
        return this.vaccineRepo.save(vaccine);
    }

    @Override
    public Vaccine get(Long id) {
        return this.vaccineRepo.findById((long) id)
                .orElseThrow(() -> new IllegalArgumentException(Message.NOT_FOUND));
    }

    @Override
    @Transactional
    public Vaccine update(Vaccine vaccine) {
        Vaccine existingVaccine = this.vaccineRepo.findById(vaccine.getId())
                .orElseThrow(() -> new IllegalArgumentException("Vaccine not found"));

        existingVaccine.setName(vaccine.getName());
        existingVaccine.setCode(vaccine.getCode());
        existingVaccine.setProtectionStartDate(vaccine.getProtectionStartDate());
        existingVaccine.setProtectionFinishDate(vaccine.getProtectionFinishDate());

        if (vaccine.getAnimal() != null && vaccine.getAnimal().getId() != null) {
            Animal animal = animalRepo.findById(vaccine.getAnimal().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Animal not found"));
            existingVaccine.setAnimal(animal);
        }

        return this.vaccineRepo.save(existingVaccine);
    }

    @Override
    public boolean delete(Long id) {
        this.get(id);
        this.vaccineRepo.deleteById(id);
        return true;
    }

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
