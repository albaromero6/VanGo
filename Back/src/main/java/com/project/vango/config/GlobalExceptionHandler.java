package com.project.vango.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;

    public GlobalExceptionHandler(MessageSource messageSource, LocaleResolver localeResolver) {
        this.messageSource = messageSource;
        this.localeResolver = localeResolver;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        String message = messageSource.getMessage("auth.forbidden", null, locale);
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex,
            HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        String message = messageSource.getMessage("auth.unauthorized", null, locale);
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex, HttpServletRequest request) {
        Locale locale = localeResolver.resolveLocale(request);
        String message = messageSource.getMessage("error.server", null, locale);
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}