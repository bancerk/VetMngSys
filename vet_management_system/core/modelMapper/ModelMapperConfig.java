package dev.patika.vet_management_system.core.modelMapper;

import dev.patika.vet_management_system.dto.response.AppointmentResponse;
import dev.patika.vet_management_system.entities.Appointment;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper getModelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<Appointment, AppointmentResponse>() {
            @Override
            protected void configure() {
                map().setAnimalId(source.getAnimal().getId());
                map().setAnimalName(source.getAnimal().getName());
                map().setDoctorId(source.getDoctor().getId());
                map().setDoctorName(source.getDoctor().getName());
            }
        });

        return modelMapper;
    }
}
