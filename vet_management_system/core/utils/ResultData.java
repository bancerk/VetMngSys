package dev.patika.vet_management_system.core.utils;

import lombok.Getter;

@Getter
public class ResultData<T> extends Result {

    private T data;

    public ResultData(boolean status, String message, String httpStatus, T data) {
        super(status, message, httpStatus);
        this.data = data;
    }
}
