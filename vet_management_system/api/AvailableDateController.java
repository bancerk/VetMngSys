package dev.patika.vet_management_system.api;

import dev.patika.vet_management_system.business.concretes.AvailableDateManager;
import dev.patika.vet_management_system.business.concretes.DoctorManager;
import dev.patika.vet_management_system.core.modelMapper.ModelMapperService;
import dev.patika.vet_management_system.core.utils.Result;
import dev.patika.vet_management_system.core.utils.ResultData;
import dev.patika.vet_management_system.core.utils.ResultHelper;
import dev.patika.vet_management_system.dto.request.AvailableDateSaveRequest;
import dev.patika.vet_management_system.dto.request.AvailableDateUpdateRequest;
import dev.patika.vet_management_system.dto.response.AvailableDateResponse;
import dev.patika.vet_management_system.entities.AvailableDate;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/availabledate")
public class AvailableDateController {
    private final AvailableDateManager availableDateService;
    private final ModelMapperService modelMapper;

    public AvailableDateController(
            AvailableDateManager availableDateService,
            DoctorManager doctorService,
            ModelMapperService modelMapper) {
        this.availableDateService = availableDateService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AvailableDateResponse> save(@Valid @RequestBody AvailableDateSaveRequest request) {
        try {
            AvailableDate availableDate = new AvailableDate();
            availableDate.setAvailableDate(request.getAvailableDate());
            availableDate.setDoctorId(request.getDoctorId());

            AvailableDate savedDate = this.availableDateService.save(availableDate);

            AvailableDateResponse response = this.modelMapper.forResponse().map(savedDate, AvailableDateResponse.class);
            return ResultHelper.created(response);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving available date: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AvailableDateResponse> findById(@PathVariable Long id) {
        AvailableDate availableDate = this.availableDateService.get(id);
        AvailableDateResponse availableDateResponse = this.modelMapper.forResponse().map(availableDate,
                AvailableDateResponse.class);
        return ResultHelper.successData(availableDateResponse);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AvailableDateResponse> update(
        @Valid @RequestBody AvailableDateUpdateRequest availableDateUpdateRequest) {
    AvailableDate updateAvailableDate = this.modelMapper.forRequest().map(availableDateUpdateRequest,
        AvailableDate.class);
    this.availableDateService.update(updateAvailableDate);
    AvailableDateResponse availableDateResponse = this.modelMapper.forResponse().map(updateAvailableDate,
        AvailableDateResponse.class);
    return ResultHelper.successData(availableDateResponse);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        this.availableDateService.delete(id);
        return ResultHelper.success();
    }
}
