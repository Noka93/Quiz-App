package com.remidiousE.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name field must not be blank")
    @Pattern(regexp = "^[A-Z][a-zA-Z]*(\\s+[A-Z][a-zA-Z]*){1,}$", message = "Please enter a valid name with first letters capitalized.")
    @Column(name = "name", unique = true, length = 150)
    private String name;

    @NotBlank(message = "Username field must not be blank")
    @Column(name = "username", unique = true, length = 50)
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

    @Column(name = "isActive")
    private boolean active;

    @Column(name = "Otp", unique = true)

    private String otp;

    @Column(name = "Otp-Generated")
    private LocalDateTime otpGeneratedTime;

    @Column(name = "Score")
    private int score;

}
