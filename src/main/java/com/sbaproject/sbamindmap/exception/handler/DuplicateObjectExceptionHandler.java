package com.sbaproject.sbamindmap.exception.handler;

import com.sbaproject.sbamindmap.dto.response.ApiResponse;
import com.sbaproject.sbamindmap.exception.DuplicateObjectException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DuplicateObjectExceptionHandler {

    @ExceptionHandler(DuplicateObjectException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateObject(DuplicateObjectException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(null, ex.getMessage()));
    }
}
