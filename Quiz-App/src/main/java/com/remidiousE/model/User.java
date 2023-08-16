package com.remidiousE.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.HashSet;
import java.util.Set;

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
    @Size(min = 2)
    @Pattern(regexp = "[A-Z][a-z]{2,}", message = "First letter must start with a capital letter and more than 2 lowercase letters")
    private String firstName;

    @NotBlank(message = "Name field must not be blank")
    @Size(min = 2)
    @Pattern(regexp = "[A-Z][a-z]{2,}", message = "First letter must start with a capital letter and more than 2 lowercase letters")
    private String lastName;

    @NotBlank(message = "Username field must not be blank")
    private String username;

    @NaturalId(mutable = true)
    @NotBlank(message = "Email field must not be blank")
    @Email(message = "Enter a valid email")
    private String email;

    @NotBlank(message = "Password field must not be blank")
////    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
//            message = "Please ensure that your password contains at least 8 characters," +
//                    " including at least one alphabetical character (uppercase or lowercase), " +
//                    "one digit, and one special character from the set [@$!%*#?&].")
    private String password;

    @Column(name = "`role`")
    private String role;

    private boolean isEnabled = false;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Quiz> quiz = new HashSet<>();
}
