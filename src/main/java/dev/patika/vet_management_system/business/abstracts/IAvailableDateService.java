package dev.patika.vet_management_system.business.abstracts;

import dev.patika.vet_management_system.entities.AvailableDate;

public interface IAvailableDateService {

    AvailableDate save(AvailableDate availableDate);

    AvailableDate get(Long id);

    AvailableDate update(AvailableDate availableDate);

    boolean delete(Long id);

}
