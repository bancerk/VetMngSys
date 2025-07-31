package dev.patika.animal_service.api;

import dev.patika.animal_service.dto.AnimalResponse;
import dev.patika.animal_service.dto.AnimalSaveRequest;
import dev.patika.animal_service.dto.AnimalUpdateRequest;
import dev.patika.animal_service.entities.Animal;
import dev.patika.animal_service.service.AnimalManager;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/animal")
public class AnimalController {

    private final AnimalManager animalService;

    public AnimalController(AnimalManager animalService) {
        this.animalService = animalService;
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AnimalResponse> save(@Valid @RequestBody AnimalSaveRequest animalSaveRequest) {
        try {
            Animal saveAnimal = this.modelMapper.forRequest().map(animalSaveRequest, Animal.class);
            saveAnimal.setCustomer(customerService.get(animalSaveRequest.getCustomerId()));
            Animal savedAnimal = this.animalService.save(saveAnimal);
            AnimalResponse animalResponse = this.modelMapper.forResponse().map(savedAnimal, AnimalResponse.class);
            return ResultHelper.created(animalResponse);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving animal: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse> findById(@PathVariable Long id) {
        Animal animal = this.animalService.get(id);
        AnimalResponse animalResponse = this.modelMapper.forResponse().map(animal, AnimalResponse.class);
        return ResultHelper.successData(animalResponse);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse> update(@Valid @RequestBody AnimalUpdateRequest animalUpdateRequest) {
        Animal updateAnimal = this.modelMapper.forRequest().map(animalUpdateRequest, Animal.class);
        this.animalService.update(updateAnimal);
        AnimalResponse animalResponse = this.modelMapper.forResponse().map(updateAnimal, AnimalResponse.class);
        return ResultHelper.successData(animalResponse);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        this.animalService.delete(id);
        return ResultHelper.success();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AnimalResponse> findByName(@RequestParam String name) {
        Optional<Animal> animal = this.animalService.getName(name);
        AnimalResponse animalResponse = this.modelMapper.forResponse().map(animal.get(), AnimalResponse.class);
        return ResultHelper.successData(animalResponse);
    }

    @GetMapping("/vaccines")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<List<VaccineResponse>> getVaccinesByAnimalName(@RequestParam String name) {
        Optional<Animal> foundAnimal = animalService.getName(name);

        if (foundAnimal.isPresent()) {
            List<VaccineResponse> vaccineResponses = foundAnimal.get().getVaccineList().stream()
                    .map(vaccine -> this.modelMapper.forResponse().map(vaccine, VaccineResponse.class))
                    .toList();
            return ResultHelper.successData(vaccineResponses);
        } else {
            return ResultHelper.successData(List.of());
        }
    }

}
