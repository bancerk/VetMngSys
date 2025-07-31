package dev.patika.vet_management_system.core.modelMapper;

import org.modelmapper.ModelMapper;

public interface IModelMapperService {

    ModelMapper forRequest();
    ModelMapper forResponse();
}
