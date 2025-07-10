package dev.patika.vet_management_system.core.utils;

public class ResultHelper {

    public static <T> ResultData<T> created(T data) {
        return new ResultData<>(true, Message.CREATED, "201", data);
    }

    public static <T> ResultData<T> validateError(T data) {
        return new ResultData<>(false, Message.BAD_REQUEST, "400", data);
    }

    public static <T> ResultData<T> successData(T data) {
        return new ResultData<>(true, Message.SUCCESS, "200", data);
    }

    public static Result notFoundError(String message) {
        return new Result(false, message, "404");
    }

    public static Result success() {
        return new Result(true, Message.SUCCESS, "200");
    }

    public static Result illegalArgument(String message) {
        return new Result(false, Message.ILLEGAL_ARGUMENT, "500");
    }
    
}
