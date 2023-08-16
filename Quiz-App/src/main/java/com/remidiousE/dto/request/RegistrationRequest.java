package com.remidiousE.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    @NotBlank(message = "Name field must not be blank")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*(\\s+[A-Z][a-zA-Z]*){1,}$", message = "Please enter a valid name with first letters capitalized.")
    @Column(name = "name", unique = true, length = 150)
    private String firstName;

    @NotBlank(message = "Name field must not be blank")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*(\\s+[A-Z][a-zA-Z]*){1,}$", message = "Please enter a valid name with first letters capitalized.")
    @Column(name = "name", unique = true, length = 150)
    private String lastName;

    private String username;

    @NotBlank(message = "Email field must not be blank")
    @Column(name = "email", unique = true, length = 150)
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "Password field must not be blank")
    @Column(name = "password", unique = true)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "Please ensure that your password contains at least 8 characters, including at least one " +
            "alphabetical character (uppercase or lowercase), one digit, and one special character from the set [@$!%*#?&].")
    private String password;

    private String role;

}
