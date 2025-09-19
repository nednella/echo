package com.example.echo_api.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

/**
 * Configuration class for Hibernate Validator API.
 */
@Configuration
public class ValidationConfig {

    /**
     * Method exposes a {@link Validator} bean, enabling configuration of the
     * default validator.
     * 
     * <p>
     * Validator is configured to fail-fast; the first validation failure
     * encountered is what is passed to the exception.
     * 
     * <p>
     * For more information, refer to:
     * {@link https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/?v=8.0#section-fail-fast}
     * 
     * @return the configured validator
     */
    @Bean
    Validator validator() {
        return Validation
            .byProvider(HibernateValidator.class)
            .configure()
            .failFast(true)
            .buildValidatorFactory()
            .getValidator();
    }

}
