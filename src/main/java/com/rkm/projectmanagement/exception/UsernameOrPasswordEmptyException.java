package com.rkm.projectmanagement.exception;

import org.springframework.security.core.AuthenticationException;

public class UsernameOrPasswordEmptyException extends AuthenticationException {
    
    public UsernameOrPasswordEmptyException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UsernameOrPasswordEmptyException(String msg) {
        super(msg);
    }
}
