package com.remidiousE.controller;

import com.remidiousE.dto.request.LoginRequest;
import com.remidiousE.dto.request.UserUpdateRequest;
import com.remidiousE.exceptions.UserNotFoundException;
import com.remidiousE.model.User;
import com.remidiousE.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String>login(@RequestBody LoginRequest loginRequest){
        return new ResponseEntity<>(userService.login(loginRequest), HttpStatus.OK);
    }
    @GetMapping("/find-user/{id}")
    public ResponseEntity<Optional<User>> findUserById(@PathVariable Long id) throws UserNotFoundException {
        return new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
    }

    @GetMapping("/findUser/{username}")
    public ResponseEntity<Optional<User>> findUserByUsername(@PathVariable String username) throws UserNotFoundException {
        return new ResponseEntity<>(userService.findUserByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/get-users")
    public ResponseEntity<List<User>> findUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
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
