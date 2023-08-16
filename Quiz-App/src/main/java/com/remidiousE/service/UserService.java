package com.remidiousE.service;

import com.remidiousE.dto.request.LoginRequest;
import com.remidiousE.dto.request.RegistrationRequest;
import com.remidiousE.dto.request.UserUpdateRequest;
import com.remidiousE.exceptions.UserAlreadyExistsException;
import com.remidiousE.exceptions.UserNotFoundException;
import com.remidiousE.model.User;
import com.remidiousE.model.VerificationToken;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();

    User registerUser(RegistrationRequest request) throws UserAlreadyExistsException;

    Optional<User> findByEmail(String email) throws UserNotFoundException;

    void saveUserVerificationToken(User theUser, String verificationToken);

    String validateToken(String verifyToken);

    void createPasswordResetTokenForUser(User user, String passwordToken);

    String validatePasswordResetToken(String passwordRestToken);

    User findUserByPasswordToken(String passwordRestToken);

    void changePassword(User user, String newPassword);

    boolean oldPasswordIsValid(User user, String oldPassword);

    VerificationToken generateNewVerificationToken(String oldToken);
    String login(LoginRequest loginRequest);

    Optional<User> findUserById(Long id) throws UserNotFoundException;

    Optional<User> findUserByUsername(String username) throws UserNotFoundException;

    User updateUser(UserUpdateRequest userUpdateRequest) throws UserNotFoundException;
    String deleteUser(String username);

}
