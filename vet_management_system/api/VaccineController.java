package dev.patika.vet_management_system.api;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import dev.patika.vet_management_system.business.concretes.AnimalManager;
import dev.patika.vet_management_system.business.concretes.VaccineManager;
import dev.patika.vet_management_system.core.modelMapper.ModelMapperService;
import dev.patika.vet_management_system.core.utils.Result;
import dev.patika.vet_management_system.core.utils.ResultData;
import dev.patika.vet_management_system.core.utils.ResultHelper;
import dev.patika.vet_management_system.dto.request.VaccineSaveRequest;
import dev.patika.vet_management_system.dto.request.VaccineUpdateRequest;
import dev.patika.vet_management_system.dto.response.AnimalVaccineExpiringResponse;
import dev.patika.vet_management_system.dto.response.VaccineResponse;
import dev.patika.vet_management_system.entities.Animal;
import dev.patika.vet_management_system.entities.Vaccine;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/vaccine")
public class VaccineController {
    private final VaccineManager vaccineService;
    private final AnimalManager animalService;
    private final ModelMapperService modelMapper;
    public VaccineController(VaccineManager vaccineService, AnimalManager animalService,
            ModelMapperService modelMapper) {
        this.vaccineService = vaccineService;
        this.animalService = animalService;
        this.modelMapper = modelMapper;
    }
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<VaccineResponse> save(@Valid @RequestBody VaccineSaveRequest vaccineSaveRequest) {
        try {
            Vaccine saveVaccine = this.modelMapper.forRequest().map(vaccineSaveRequest, Vaccine.class);

            Animal animal = animalService.get(vaccineSaveRequest.getAnimalId());
            saveVaccine.setAnimal(animal);

            Vaccine savedVaccine = this.vaccineService.save(saveVaccine);

            VaccineResponse vaccineResponse = this.modelMapper.forResponse().map(savedVaccine, VaccineResponse.class);
            vaccineResponse.setRegistrationAllowed(savedVaccine.isRegistrationAllowed());
            return ResultHelper.created(vaccineResponse);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving vaccine: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<VaccineResponse> findById(@PathVariable Long id) {
        Vaccine vaccine = this.vaccineService.get(id);
        VaccineResponse vaccineResponse = this.modelMapper.forResponse().map(vaccine, VaccineResponse.class);
        return ResultHelper.successData(vaccineResponse);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<VaccineResponse> update(@Valid @RequestBody VaccineUpdateRequest vaccineUpdateRequest) {
        try {
            vaccineService.get(vaccineUpdateRequest.getId());

            Vaccine updateVaccine = this.modelMapper.forRequest().map(vaccineUpdateRequest, Vaccine.class);

            Animal animal = animalService.get(vaccineUpdateRequest.getAnimalId());
            updateVaccine.setAnimal(animal);

            Vaccine updatedVaccine = this.vaccineService.update(updateVaccine);

            VaccineResponse vaccineResponse = this.modelMapper.forResponse().map(updatedVaccine, VaccineResponse.class);
            return ResultHelper.successData(vaccineResponse);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating vaccine: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        this.vaccineService.delete(id);
        return ResultHelper.success();
    }

    @GetMapping("/expiring-in-two-weeks")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<AnimalVaccineExpiringResponse>> getAnimalsWithVaccineExpiringInTwoWeeks() {
        List<AnimalVaccineExpiringResponse> responses = vaccineService.getAnimalsWithVaccineExpiringInTwoWeeks();
        return ResultHelper.successData(responses);
    }

    @GetMapping("/animal")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> getVaccinesByAnimalName(@RequestParam("name") String animalName) {
        Optional<Animal> foundAnimalName = animalService.getName(animalName);

    if (foundAnimalName.isPresent()) {
            List<VaccineResponse> vaccineResponses = foundAnimalName.get().getVaccineList().stream()
                    .map(vaccine -> this.modelMapper.forResponse().map(vaccine, VaccineResponse.class))
                    .toList();
            return ResultHelper.successData(vaccineResponses);
        } else {
            throw new IllegalArgumentException("Animal not found with name: " + animalName);
        }
    }
}