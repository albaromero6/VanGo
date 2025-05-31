package com.project.vango.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {
        private static final Logger logger = LoggerFactory.getLogger(TestController.class);

        private final MessageSource messageSource;
        private final LocaleResolver localeResolver;

        public TestController(MessageSource messageSource, LocaleResolver localeResolver) {
                this.messageSource = messageSource;
                this.localeResolver = localeResolver;
        }

        @GetMapping("/messages")
        public ResponseEntity<Map<String, String>> getMessages(HttpServletRequest request) {
                try {
                        logger.info("Starting getMessages method");

                        // Obtener y loguear el locale
                        Locale locale = localeResolver.resolveLocale(request);
                        logger.info("Resolved locale: {}", locale);

                        Map<String, String> messages = new HashMap<>();

                        // Probar cada mensaje individualmente
                        try {
                                String requiredMsg = messageSource.getMessage("validation.required", null, locale);
                                logger.info("Got validation.required message: {}", requiredMsg);
                                messages.put("validation.required", requiredMsg);
                        } catch (Exception e) {
                                logger.error("Error getting validation.required message", e);
                        }

                        try {
                                String emailMsg = messageSource.getMessage("validation.email", null, locale);
                                logger.info("Got validation.email message: {}", emailMsg);
                                messages.put("validation.email", emailMsg);
                        } catch (Exception e) {
                                logger.error("Error getting validation.email message", e);
                        }

                        try {
                                String notFoundMsg = messageSource.getMessage("error.not.found", null, locale);
                                logger.info("Got error.not.found message: {}", notFoundMsg);
                                messages.put("error.not.found", notFoundMsg);
                        } catch (Exception e) {
                                logger.error("Error getting error.not.found message", e);
                        }

                        try {
                                String serverErrorMsg = messageSource.getMessage("error.server", null, locale);
                                logger.info("Got error.server message: {}", serverErrorMsg);
                                messages.put("error.server", serverErrorMsg);
                        } catch (Exception e) {
                                logger.error("Error getting error.server message", e);
                        }

                        try {
                                String unauthorizedMsg = messageSource.getMessage("auth.unauthorized", null, locale);
                                logger.info("Got auth.unauthorized message: {}", unauthorizedMsg);
                                messages.put("auth.unauthorized", unauthorizedMsg);
                        } catch (Exception e) {
                                logger.error("Error getting auth.unauthorized message", e);
                        }

                        try {
                                String forbiddenMsg = messageSource.getMessage("auth.forbidden", null, locale);
                                logger.info("Got auth.forbidden message: {}", forbiddenMsg);
                                messages.put("auth.forbidden", forbiddenMsg);
                        } catch (Exception e) {
                                logger.error("Error getting auth.forbidden message", e);
                        }

                        logger.info("Successfully created messages map: {}", messages);
                        return ResponseEntity.ok(messages);

                } catch (Exception e) {
                        logger.error("Unexpected error in getMessages", e);
                        throw e;
                }
        }

        @GetMapping("/error-test")
        public ResponseEntity<String> testError() {
                throw new RuntimeException("Test error");
        }
}