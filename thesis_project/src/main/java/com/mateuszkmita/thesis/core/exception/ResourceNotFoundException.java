package com.mateuszkmita.thesis.core.exception;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
