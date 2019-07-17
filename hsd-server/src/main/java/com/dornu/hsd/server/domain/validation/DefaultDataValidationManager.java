package com.dornu.hsd.server.domain.validation;

import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DefaultDataValidationManager<T> implements ValidationManager<T> {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Override
    public Set<String> validate(T requestModel) {
        Set<ConstraintViolation<T>> violations = validator.validate(requestModel);
        if (!violations.isEmpty()) {
            return violations.stream()
                    .map(x-> x.getPropertyPath() + ": " + x.getMessage())
                    .collect(Collectors.toSet());
        } else return Collections.emptySet();
    }
}
