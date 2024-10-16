package com.project.gymweb.exceptions;

public class RoutineNotFoundException extends RuntimeException {
    public RoutineNotFoundException(String message) {
        super(message);
    }

    public RoutineNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
