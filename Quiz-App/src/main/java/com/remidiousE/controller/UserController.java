package com.remidiousE.controller;

import com.remidiousE.dto.request.UserRegistrationRequest;
import com.remidiousE.dto.request.UserUpdateRequest;
import com.remidiousE.dto.response.UserRegistrationResponse;
import com.remidiousE.exceptions.UserNotFoundException;
import com.remidiousE.model.User;
import com.remidiousE.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
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
    @GetMapping("/findUser/{id}")
    public Optional<User> findUserById(@PathVariable Long id) throws UserNotFoundException {
        return userService.findUserById(id);
    }

    @GetMapping("/findUser/{username}")
    public Optional<User> findUserByUsername(@PathVariable String username) throws UserNotFoundException {
        return userService.findUserByUsername(username);
    }

    @GetMapping("/get-users")
    public List<User> findAllUsers(){
        return userService.findAllUsers();
    }

    @PutMapping("/updateUser")
    public User updateUser(@RequestBody UserUpdateRequest userUpdateRequest) throws UserNotFoundException {
        return userService.updateUser(userUpdateRequest);
    }

    @DeleteMapping("/deleteUser")
    public String deleteUser(@RequestParam String username){
        return userService.deleteUser(username);
    }
}
