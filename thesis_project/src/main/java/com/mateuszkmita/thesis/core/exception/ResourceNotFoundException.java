package com.mateuszkmita.thesis.core.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends Exception {

    private final String resourceName;
    private final int id;

    public ResourceNotFoundException(String resourceName, int id) {
        this.id = id;
        this.resourceName = resourceName;
    }
}
