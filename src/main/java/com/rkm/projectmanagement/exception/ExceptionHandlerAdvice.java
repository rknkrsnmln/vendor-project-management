package com.rkm.projectmanagement.exception;

import com.rkm.projectmanagement.dtos.ResultBaseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler({ProjectNotFoundException.class, VendorNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleUser(Exception e) {
        return new ResponseEntity<>(ResultBaseDto.builder()
                .flag(false)
                .message(e.getMessage())
                .code(HttpStatus.NOT_FOUND.value())
                .data(null)
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleUser(ObjectNotFoundException e) {
        return new ResponseEntity<>(ResultBaseDto.builder()
                .flag(false)
                .message(e.getMessage())
                .code(HttpStatus.NOT_FOUND.value())
                .data(null)
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleUser(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        Map<String, String> errorMap = new HashMap<>(errors.size());
        errors.forEach((error) -> {
            String key = ((FieldError) error).getField();
            String val = error.getDefaultMessage();
            errorMap.put(key, val);
        });
        return new ResponseEntity<>(ResultBaseDto.builder()
                .flag(false)
//                .message(e.getMessage())
                .message("Provided arguments are invalid, see data for details.")
                .code(HttpStatus.NOT_FOUND.value())
                .data(errorMap)
                .build(), HttpStatus.NOT_FOUND);
    }

}
