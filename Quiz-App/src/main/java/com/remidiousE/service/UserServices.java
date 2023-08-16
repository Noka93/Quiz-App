package com.remidiousE.service;

import com.remidiousE.dto.request.LoginRequest;
import com.remidiousE.dto.request.RegistrationRequest;
import com.remidiousE.dto.request.UserUpdateRequest;
import com.remidiousE.dto.response.RegistrationResponse;
import com.remidiousE.exceptions.UserAlreadyExistsException;
import com.remidiousE.exceptions.UserNotFoundException;
import com.remidiousE.model.User;
import com.remidiousE.model.VerificationToken;
import com.remidiousE.repository.UserRepository;
import com.remidiousE.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServices implements UserService{

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final VerificationTokenRepository tokenRepository;

    private final PasswordResetTokenServices resetTokenServices;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registerUser(RegistrationRequest request) throws UserAlreadyExistsException {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isPresent()) throw new UserAlreadyExistsException("User with email: " + request.getEmail() + " already exist");
        var newUser = new User();
        modelMapper.map(request, newUser);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        var savedUser = userRepository.save(newUser);
        RegistrationResponse response = modelMapper.map(savedUser, RegistrationResponse.class);
        return savedUser;
    }
    @Override
    public Optional<User> findByEmail(String email) throws UserNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) throw new UserNotFoundException("User not found with " + email);
        else{
            return user;
        }
    }
    @Override
    public void saveUserVerificationToken(User theUser, String verifyToken) {
        var verificationToken = new VerificationToken(verifyToken, theUser);
        tokenRepository.save(verificationToken);
    }
    @Override
    public String validateToken(String verifyToken) {
        VerificationToken token = tokenRepository.findByToken(verifyToken);
        if (token == null){
            return "Invalid verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
//            tokenRepository.delete(token);
            return "Verification link already expired, "+
                    " Please, click on the link below to receive a new verification link";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }
    @Override
    public void createPasswordResetTokenForUser(User user, String passwordToken) {
        resetTokenServices.createPasswordResetToken(user, passwordToken);
    }
    @Override
    public String validatePasswordResetToken(String passwordRestToken) {
        return resetTokenServices.validatePasswordRestToken(passwordRestToken);
    }
    @Override
    public User findUserByPasswordToken(String passwordRestToken) {
        return resetTokenServices.findUserByPasswordToken(passwordRestToken).get();
    }
    @Override
    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean oldPasswordIsValid(User user, String oldPassword){
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = tokenRepository.findByToken(oldToken);
        var verificationTokenTime = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpirationTime(verificationTokenTime.getTokenExpirationTime());
        return tokenRepository.save(verificationToken);
    }

    @Override
    public String login(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public Optional<User> findUserById(Long id) throws UserNotFoundException {
        Optional<User> foundUser = userRepository.findById(id);
        if (foundUser.isEmpty())throw new UserNotFoundException("User with " + id + " not found");
        else return foundUser;
    }

    @Override
    public Optional<User> findUserByUsername(String username) throws UserNotFoundException {
        Optional<User> foundUser = userRepository.findUserByUsername(username);
        if (foundUser.isEmpty())throw new UserNotFoundException("User with " + username + " not found");
        return foundUser;
    }

    @Override
    public User updateUser(UserUpdateRequest userUpdateRequest) throws UserNotFoundException {
        User foundUser = new User();
        if(userUpdateRequest.getFirstName() != null && !userUpdateRequest.getFirstName().isEmpty())
            foundUser.setFirstName(userUpdateRequest.getFirstName());
        if(userUpdateRequest.getLastName() != null && !userUpdateRequest.getLastName().isEmpty())
            foundUser.setLastName(userUpdateRequest.getLastName());
        if(userUpdateRequest.getUsername() != null && !userUpdateRequest.getUsername().isEmpty())
            foundUser.setUsername(userUpdateRequest.getUsername());
        if(userUpdateRequest.getEmail() != null && !userUpdateRequest.getEmail().isEmpty())
            foundUser.setEmail(userUpdateRequest.getEmail());
        return userRepository.save(foundUser);
    }

    @Override
    public String deleteUser(String username) {
        userRepository.deleteByUsername(username);
        return "User has been deleted successfully";
    }
}

