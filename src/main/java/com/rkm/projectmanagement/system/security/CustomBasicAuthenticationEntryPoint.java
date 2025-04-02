package com.rkm.projectmanagement.system.security;

import com.rkm.projectmanagement.exception.UsernameOrPasswordEmptyException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Objects;

/**
 * This class handles unsuccessful basic authentication.
 * We implement AuthenticationEntryPoint and then delegate the exception handler to HandlerExceptionResolver.
 */
@Component
public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /*
     * Here we've injected the DefaultHandlerExceptionResolver and delegated the handler to this resolver.
     * This security exception can now be handled with controller advice with an exception handler method.
     */
    private final HandlerExceptionResolver handlerExceptionResolver;

    public CustomBasicAuthenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.addHeader("WWW-Authenticate", "Basic realm=\"" + authException.getMessage() + "\"");
        if (Objects.equals(request.getHeader("Authorization"), "Basic Og==")) {
            this.handlerExceptionResolver.resolveException(request, response, null,  new UsernameOrPasswordEmptyException("Username and password is empty"));
        } else {
            this.handlerExceptionResolver.resolveException(request, response, null, authException);
        }
    }
}
