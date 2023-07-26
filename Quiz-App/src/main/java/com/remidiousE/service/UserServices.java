package com.remidiousE.service;

import com.remidiousE.dto.request.LoginRequest;
import com.remidiousE.dto.request.UserRegistrationRequest;
import com.remidiousE.dto.request.UserUpdateRequest;
import com.remidiousE.dto.response.UserRegistrationResponse;
import com.remidiousE.exceptions.CustomException;
import com.remidiousE.exceptions.UserNotFoundException;
import com.remidiousE.model.User;
import com.remidiousE.repository.UserRepository;
import com.remidiousE.utils.MailSender;
import com.remidiousE.utils.OtpUtils;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Data
public class UserServices implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    private OtpUtils otpUtils;

    @Resource
    private MailSender mailSender;

    @Override
    public ResponseEntity<UserRegistrationResponse> registerUser(UserRegistrationRequest request) {
        String otp = otpUtils.generateOtp();
        ifUserAlreadyExist(request.getEmail(), request.getUsername(), request.getPassword());
        try {
            mailSender.sendOtpEmail(request.getEmail(), otp);
        } catch (MessagingException e) {
            throw new CustomException("Unable to send OTP. Please try again.");
        }
        User user = new User();
        modelMapper.map(request, user);
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        UserRegistrationResponse response = modelMapper.map(savedUser, UserRegistrationResponse.class);
        response.setMessage("Welcome " + savedUser.getName() + "," + " You have successfully registered");
        return ResponseEntity.ok(response);
    }
    @Override
    public String verifyAccount(String email, String otp) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new CustomException("User not found with this email: " + email));
        if(user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (1 * 60)){
            user.setActive(true);
            userRepository.save(user);
            return "OTP verified, you can now login";
        }
        return "Please regenerate otp and try again";
    }
private void ifUserAlreadyExist(String email, String username, String password){
        Optional<User> foundUserByEmail = userRepository.findByEmail(email);
        Optional<User>foundUserByUsername = userRepository.findUserByUsername(username);
        Optional<User>foundUserByPassword = userRepository.findByPassword(password);

       if (foundUserByEmail.isPresent())throw new CustomException("User with this email already exist");
       if (foundUserByUsername.isPresent()) throw new CustomException("User with this username already exist");
       if (foundUserByPassword.isPresent()) throw new CustomException("User with this password already exist");
    }
    @Override
    public String regenerateOtp(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()-> new CustomException("User not found with this email: " + email));

        String otp = otpUtils.generateOtp();
        try {
            mailSender.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new CustomException("Unable to send OTP. Please try again.");
        }
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);
        return "Email sent... please verify account within 1 minute";
    }
    @Override
    public String login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(
                        () -> new CustomException("User not found with this email:" + loginRequest.getEmail()));
        if (!loginRequest.getPassword().equals(user.getPassword())){
            return "password is incorrect";
        }else if(!user.isActive()){
            return "Your account is not verified";
        }
        return "Login successful";
    }
    @Override
    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new CustomException("User not found with this email:" + email));
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
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
    @Override
    public User updateUser(UserUpdateRequest userUpdateRequest) throws UserNotFoundException {
        Optional<User> existingUser = userRepository.findByEmail(userUpdateRequest.getEmail());
        if (existingUser.isEmpty())throw new UserNotFoundException("User not found");
        User foundUser =existingUser.get();
        if(userUpdateRequest.getName() != null && !userUpdateRequest.getName().isEmpty())
            foundUser.setName(userUpdateRequest.getName());
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
