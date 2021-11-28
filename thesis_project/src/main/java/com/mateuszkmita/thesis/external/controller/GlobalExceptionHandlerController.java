package com.mateuszkmita.thesis.external.controller;

import com.mateuszkmita.thesis.core.exception.InvalidInputResourceException;
import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.external.controller.dto.ProcedureResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler({
            ResourceNotFoundException.class
    })
    @ResponseBody
    public ResponseEntity<ProcedureResultDto> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
        ProcedureResultDto procedureResultDto = new ProcedureResultDto();
        procedureResultDto.setMessage("Resource " + ex.getResourceName() + " with ID " + ex.getId() + " not found!");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(procedureResultDto);
    }

    @ExceptionHandler({
            InvalidInputResourceException.class
    })
    @ResponseBody
    public ResponseEntity<ProcedureResultDto> invalidInputResourceExceptionHandler(InvalidInputResourceException ex) {
        ProcedureResultDto procedureResultDto = new ProcedureResultDto();
        procedureResultDto.setMessage("Input data is invalid! " + ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(procedureResultDto);
    }
}
