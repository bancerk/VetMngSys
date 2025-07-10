package dev.patika.vet_management_system.core.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Result {

    private boolean status;

    private  String message;

    private String httpStatus;

}
