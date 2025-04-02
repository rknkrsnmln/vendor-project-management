package com.rkm.projectmanagement.controller;

import com.rkm.projectmanagement.dtos.ResultBaseDto;
import com.rkm.projectmanagement.exception.UsernameOrPasswordEmptyException;
import com.rkm.projectmanagement.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("${api.endpoint.base-url}")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/users/login")
    public ResponseEntity<ResultBaseDto<Map<String, Object>>> getLoginInfo(Authentication authentication) {

        logger.debug("Authentication for user: '{}'", authentication.getName());
        return new ResponseEntity<>(ResultBaseDto.<Map<String, Object>>builder()
                .flag(true)
                .message("User Info and JSON Web Token")
                .data(this.authService.createLoginInfo(authentication))
                .code(HttpStatus.OK.value())
                .build(), HttpStatus.OK);
    }
}
