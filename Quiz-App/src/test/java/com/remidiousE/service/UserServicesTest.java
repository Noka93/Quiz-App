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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest

class UserServicesTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OtpUtils otpUtils;

    @Resource
    private MailSender mailSender;

    @Autowired
    private UserServices userServices;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        modelMapper = new ModelMapper();
        otpUtils = Mockito.mock(OtpUtils.class);
        mailSender = Mockito.mock(MailSender.class);
        userServices = new UserServices(userRepository, modelMapper);
        userServices.setOtpUtils(otpUtils);
        userServices.setMailSender(mailSender);
    }

    @Test
    void testRegisterUser() throws MessagingException {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail("test@example.com");
        request.setName("Test User");
        request.setPassword("password");

        User savedUser = new User();
        savedUser.setUserId(1L);
        savedUser.setEmail(request.getEmail());
        savedUser.setName(request.getName());
        savedUser.setPassword(request.getPassword());
        savedUser.setActive(false);

        String otp = "123456";
        LocalDateTime otpGeneratedTime = LocalDateTime.now();

        Mockito.when(otpUtils.generateOtp()).thenReturn(otp);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);

        ResponseEntity<UserRegistrationResponse> response = userServices.registerUser(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(200, response.getStatusCode());
        UserRegistrationResponse responseBody = response.getBody();
        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals("Welcome Test User, You have successfully registered", responseBody.getMessage());
        Assertions.assertNotNull(responseBody.getMessage());

        Mockito.verify(otpUtils).generateOtp();
        Mockito.verify(mailSender).sendOtpEmail("test@example.com", otp);
        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    void testVerifyAccount() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setOtp("123456");
        user.setOtpGeneratedTime(LocalDateTime.now().minusSeconds(30));

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));

        String result = userServices.verifyAccount("test@example.com", "123456");

        Assertions.assertEquals("OTP verified, you can now login", result);
        Mockito.verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void testRegenerateOtp() throws MessagingException {
        User user = new User();
        user.setEmail("test@example.com");
        String otp = "123456";
        LocalDateTime otpGeneratedTime = LocalDateTime.now();

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        Mockito.when(otpUtils.generateOtp()).thenReturn(otp);

        String result = userServices.regenerateOtp("test@example.com");
        Assertions.assertEquals("Email sent... please verify account within 1 minute", result);

        Mockito.verify(userRepository).findByEmail("test@example.com");
        Mockito.verify(otpUtils).generateOtp();
        Mockito.verify(mailSender).sendOtpEmail("test@example.com", otp);
        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    void testLogin() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setActive(true);

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));

        String result = userServices.login(new LoginRequest("test@example.com", "password"));

        Assertions.assertEquals("Login successful", result);

        Mockito.verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void testForgotPassword() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
        String result = userServices.forgotPassword("test@example.com");

        Assertions.assertNull(result);

        Mockito.verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void testFindUserById() throws UserNotFoundException {
        User user = new User();
        user.setUserId(1L);
        user.setName("Test User");

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Optional<User> result = userServices.findUserById(1L);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(user, result.get());
        Mockito.verify(userRepository).findById(1L);
    }

    @Test
    void testFindUserByUsername() throws UserNotFoundException {
        User user = new User();
        user.setUserId(1L);
        user.setName("Test User");
        user.setUsername("testuser");

        Mockito.when(userRepository.findUserByUsername(Mockito.anyString())).thenReturn(Optional.of(user));

        Optional<User> result = userServices.findUserByUsername("remy");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(user, result.get());

        Mockito.verify(userRepository).findUserByUsername("remy");
    }

    @Test
    void testFindAllUsers() {
        User user1 = new User();
        user1.setUserId(1L);
        user1.setName("Test User 1");

        User user2 = new User();
        user2.setUserId(2L);
        user2.setName("Test User 2");

        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> result = userServices.findAllUsers();

        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(user1));
        Assertions.assertTrue(result.contains(user2));

        Mockito.verify(userRepository).findAll();
    }

    @Test
    void testUpdateUser() throws UserNotFoundException {
        User existingUser = new User();
        existingUser.setUserId(1L);
        existingUser.setEmail("test@example.com");

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setEmail("updated@example.com");

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(existingUser);

        User result = userServices.updateUser(userUpdateRequest);

        Assertions.assertEquals(existingUser, result);
        Assertions.assertEquals(userUpdateRequest.getEmail(), result.getEmail());

        Mockito.verify(userRepository).findByEmail("updated@example.com");
        Mockito.verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    void testDeleteUser() {
        String username = "testuser";

        String result = userServices.deleteUser(username);

        Assertions.assertEquals("User has been deleted successfully", result);

        Mockito.verify(userRepository).deleteByUsername(username);
    }
}
