package com.remidiousE.service;

import com.remidiousE.dto.request.LoginRequest;
import com.remidiousE.dto.request.RegistrationRequest;
import com.remidiousE.dto.request.UserUpdateRequest;
import com.remidiousE.dto.response.RegistrationResponse;
import com.remidiousE.exceptions.UserNotFoundException;
import com.remidiousE.repository.UserRepository;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("remy@gmail.com");
        request.setName("remy94");
        request.setPassword("password");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(request.getEmail());
        savedUser.setName(request.getName());
        savedUser.setPassword(request.getPassword());
        savedUser.setActive(false);

        String otp = "123456";
        LocalDateTime otpGeneratedTime = LocalDateTime.now();

        when(otpUtils.generateOtp()).thenReturn(otp);
        when(userRepository.save(Mockito.any(User.class))).thenReturn(savedUser);

        ResponseEntity<RegistrationResponse> response = userServices.registerUser(request);

        Assertions.assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RegistrationResponse responseBody = response.getBody();
        Assertions.assertNotNull(responseBody);
        assertEquals("Welcome remy94, You have successfully registered", responseBody.getMessage());
        Assertions.assertNotNull(responseBody.getMessage());

        verify(otpUtils).generateOtp();
        verify(mailSender).sendOtpEmail("remy@gmail.com", otp);
        verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    void testVerifyAccount() {
        User user = new User();
        user.setEmail("remy@gmail.com");
        user.setOtp("123456");
        user.setOtpGeneratedTime(LocalDateTime.now().minusSeconds(30));

        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));

        String result = userServices.verifyAccount("remy@gmail.com", "123456");

        assertEquals("OTP verified, you can now login", result);
        verify(userRepository).findByEmail("remy@gmail.com");
    }

    @Test
    void testRegenerateOtp() throws MessagingException {
        User user = new User();
        user.setEmail("remy@gmail.com");
        String otp = "123456";
        LocalDateTime otpGeneratedTime = LocalDateTime.now();

        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));
        when(otpUtils.generateOtp()).thenReturn(otp);

        String result = userServices.regenerateOtp("remy@gmail.com");
        assertEquals("Email sent... please verify account within 1 minute", result);

        verify(userRepository).findByEmail("remy@gmail.com");
        verify(otpUtils).generateOtp();
        verify(mailSender).sendOtpEmail("remy@gmail.com", otp);
        verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    void testLogin() {
        User user = new User();
        user.setEmail("remy@gmail.com");
        user.setPassword("password");
        user.setActive(true);

        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(user));

        String result = userServices.login(new LoginRequest("remy@gmail.com", "password"));

        assertEquals("Login successful", result);

        verify(userRepository).findByEmail("remy@gmail.com");
    }

    @Test
    public void testForgotPassword() throws MessagingException {
        String email = "remy@gmail.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        doNothing().when(mailSender).sendSetPasswordEmail(email);

        Map<String, String> request = new HashMap<>();
        request.put("email", email);
        String result = userServices.forgotPassword(request);

        assertEquals("Please check your email to set a new password", result);
        verify(userRepository, times(1)).findByEmail(email);
        verify(mailSender, times(1)).sendSetPasswordEmail(email);
    }

    @Test
    void testFindUserById() throws UserNotFoundException {
        User user = new User();
        user.setId(1L);
        user.setName("remy94");

        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Optional<User> result = userServices.findUserById(1L);

        Assertions.assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository).findById(1L);
    }

    @Test
    void testFindUserByUsername() throws UserNotFoundException {
        User user = new User();
        user.setId(1L);
        user.setName("Remidious Enefola");
        user.setUsername("remy94");

        when(userRepository.findUserByUsername(Mockito.anyString())).thenReturn(Optional.of(user));

        Optional<User> result = userServices.findUserByUsername("remy94");

        Assertions.assertTrue(result.isPresent());
        assertEquals(user, result.get());

        verify(userRepository).findUserByUsername("remy94");
    }

    @Test
    void testFindAllUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("User 1");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("User 2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> result = userServices.findAllUsers();

        assertEquals(2, result.size());
        Assertions.assertTrue(result.contains(user1));
        Assertions.assertTrue(result.contains(user2));

        verify(userRepository).findAll();
    }

    @Test
    void testUpdateUser() throws UserNotFoundException {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("remy@gmail.com");

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setEmail("remi@gmail.com");

        when(userRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(existingUser);

        User result = userServices.updateUser(userUpdateRequest);

        assertEquals(existingUser, result);
        assertEquals(userUpdateRequest.getEmail(), result.getEmail());

        verify(userRepository).findByEmail("remi@gmail.com");
        verify(userRepository).save(Mockito.any(User.class));
    }

    @Test
    void testDeleteUser() {
        String username = "remy94";

        String result = userServices.deleteUser(username);

        assertEquals("User has been deleted successfully", result);

        verify(userRepository).deleteByUsername(username);
    }
}
