package com.adn.cloneig.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.adn.cloneig.validator.EmailUniqueConstraint;
import com.adn.cloneig.validator.PasswordEqualConstraint;
import com.adn.cloneig.validator.UsernameUniqueConstraint;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@PasswordEqualConstraint
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RegisterRequestDTO {
    
    @NotBlank
    @Email
    @EmailUniqueConstraint
    private String email;

    @NotBlank
    @UsernameUniqueConstraint
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;

}
