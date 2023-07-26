package com.remidiousE.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @NotBlank(message = "Name field must not be blank")
    @Size(min = 2)
    @Pattern(regexp = "[A-Z]{1}[a-z]{2,}", message = "First letter must start with capital letter and more than 3 digits")
    private String name;

    @NotBlank(message = "Username field must not be blank")
    private String username;

    @NotBlank(message = "Email field must not be blank")
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "Password field must not be blank")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", message = "Please ensure that your password contains at least 8 characters, including at least one " +
            "alphabetical character (uppercase or lowercase), one digit, and one special character from the set [@$!%*#?&].")
    private String password;
}
