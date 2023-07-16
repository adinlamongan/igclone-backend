package com.adn.cloneig.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.adn.cloneig.dto.RegisterRequestDTO;


public class PasswordEqualConstraintValidator implements ConstraintValidator<PasswordEqualConstraint, Object> {

    @Override
    public boolean isValid(Object data, ConstraintValidatorContext context) {
        RegisterRequestDTO registerRequestDTO = (RegisterRequestDTO) data;
        return registerRequestDTO.getConfirmPassword().equals(registerRequestDTO.getPassword());
    }
    
}
