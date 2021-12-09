package com.mateuszkmita.thesis.external.controller;

import com.mateuszkmita.thesis.core.exception.ResourceNotFoundException;
import com.mateuszkmita.thesis.external.controller.dto.ProcedureResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
            IllegalArgumentException.class
    })
    @ResponseBody
    public ResponseEntity<ProcedureResultDto> illegalArgumentExceptionHandler(IllegalArgumentException ex) {
        ProcedureResultDto procedureResultDto = new ProcedureResultDto();
        procedureResultDto.setMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(procedureResultDto);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    @ResponseBody
    public ResponseEntity<ProcedureResultDto> invalidRequestDataExceptionHandler(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        ProcedureResultDto procedureResultDto = new ProcedureResultDto();
        procedureResultDto.setMessage(bindingResult.getFieldError().getField() + ": " + bindingResult.getFieldError().getDefaultMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(procedureResultDto);
    }
}
