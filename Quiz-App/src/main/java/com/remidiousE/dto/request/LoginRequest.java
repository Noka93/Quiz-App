package com.remidiousE.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Email field must not be blank")
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "Password field must not be blank")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "Please ensure that your password contains at least 8 characters, including at least one " +
            "alphabetical character (uppercase or lowercase), one digit, and one special character from the set [@$!%*#?&].")
    private String password;
}
