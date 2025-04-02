package com.rkm.projectmanagement.exception;

import com.rkm.projectmanagement.dtos.ResultBaseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
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

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResultBaseDto<String>> handleUsernameAndPassword(Exception e) {
        return new ResponseEntity<>(ResultBaseDto.<String>builder()
                .flag(false)
                .message("Authentication failed")
//                .data("Username or Password is incorrect")
                .data(e.getMessage())
                .code(HttpStatus.UNAUTHORIZED.value())
                .build(), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(value = {UsernameOrPasswordEmptyException.class})
    public ResponseEntity<Object> handleUsernameOrPasswordEmpty(UsernameOrPasswordEmptyException e) {
        return new ResponseEntity<>(ResultBaseDto.builder()
                .flag(false)
                .message("Username or Password cannot be empty")
                .data(e.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AccountStatusException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResultBaseDto<String>> handleAccountStatusException(AccountStatusException e) {
        return new ResponseEntity<>(ResultBaseDto.<String>builder()
                .flag(false)
                .message("User is restricted")
                .data("Your account is disabled")
                .code(HttpStatus.UNAUTHORIZED.value())
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({InvalidBearerTokenException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResultBaseDto<String>> handleInvalidBearerTokenException(InvalidBearerTokenException e) {
        return new ResponseEntity<>(ResultBaseDto.<String>builder()
                .flag(false)
                .message("Access token is invalid")
                .data("Unauthorized access using invalid or expired token")
                .code(HttpStatus.UNAUTHORIZED.value())
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResultBaseDto<String>> handleAccessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>(ResultBaseDto.<String>builder()
                .flag(false)
                .message("Access forbidden")
                .data("Error 403 - you don't have permission to access this resource.")
                .code(HttpStatus.FORBIDDEN.value())
                .build(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResultBaseDto<Object>> handleAccessDeniedException(Exception e) {
        return new ResponseEntity<>(ResultBaseDto.<Object>builder()
                .flag(false)
                .message("A server internal error server")
                .data(null)
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
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
