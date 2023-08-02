package com.remidiousE.service;

import com.remidiousE.dto.request.LoginRequest;
import com.remidiousE.dto.request.UserRegistrationRequest;
import com.remidiousE.dto.request.UserUpdateRequest;
import com.remidiousE.dto.response.UserRegistrationResponse;
import com.remidiousE.exceptions.UserNotFoundException;
import com.remidiousE.model.User;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    ResponseEntity<UserRegistrationResponse> registerUser(UserRegistrationRequest request);

    String verifyAccount(String email, String otp);

    String regenerateOtp(String email);

    String login(LoginRequest loginRequest);

    String forgotPassword(Map<String, String> request) throws MessagingException;

    Optional<User> findUserById(Long id) throws UserNotFoundException;
    Optional<User> findUserByUsername(String username) throws UserNotFoundException;
    List<User> findAllUsers();
    User updateUser(UserUpdateRequest userUpdateRequest) throws UserNotFoundException;

    String deleteUser(String username);
}
