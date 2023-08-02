package com.remidiousE.controller;

import com.remidiousE.dto.request.LoginRequest;
import com.remidiousE.dto.request.UserRegistrationRequest;
import com.remidiousE.dto.request.UserUpdateRequest;
import com.remidiousE.dto.response.LoginResponse;
import com.remidiousE.dto.response.UserRegistrationResponse;
import com.remidiousE.exceptions.CustomException;
import com.remidiousE.exceptions.UserNotFoundException;
import com.remidiousE.model.User;
import com.remidiousE.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse>register(@RequestBody UserRegistrationRequest request){
        UserRegistrationResponse response = userService.registerUser(request).getBody();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String email, @RequestParam String otp){
        return new ResponseEntity<>(userService.verifyAccount(email, otp), HttpStatus.OK);
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        try {
            String response = userService.forgotPassword(request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (MessagingException e) {
            return new ResponseEntity<>("Unable to send set password email, please try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/generate-otp")
    public ResponseEntity<String> generateOtp(@RequestBody String email) {
        return new ResponseEntity<>(userService.regenerateOtp(email), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String>login(@RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(userService.login(loginRequest), HttpStatus.OK);
    }
    @GetMapping("/findUser/{id}")
    public ResponseEntity<Optional<User>> findUserById(@PathVariable Long id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
    }

    @GetMapping("/findUser/{username}")
    public ResponseEntity<Optional<User>> findUserByUsername(@PathVariable String username) throws UserNotFoundException {
        return new ResponseEntity<>(userService.findUserByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/get-users")
    public ResponseEntity<List<User>> findAllUsers(){
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<User> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) throws UserNotFoundException {
        return new ResponseEntity<>(userService.updateUser(userUpdateRequest), HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<String> deleteUser(@RequestParam String username){
        return new ResponseEntity<>(userService.deleteUser(username), HttpStatus.OK);
    }
}
