package com.project.vango.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class EnvConfig {

    private static final Logger logger = LoggerFactory.getLogger(EnvConfig.class);

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @PostConstruct
    public void init() {
        logger.info("DB_PASSWORD está configurado: {}", dbPassword != null && !dbPassword.isEmpty());
        logger.info("JWT_SECRET está configurado: {}", jwtSecret != null && !jwtSecret.isEmpty());
    }
}
