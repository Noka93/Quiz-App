package com.remidiousE.controller;

import com.remidiousE.dto.request.PasswordRequest;
import com.remidiousE.dto.request.RegistrationRequest;
import com.remidiousE.event.RegistrationCompleteEvent;
import com.remidiousE.event.RegistrationCompleteEventListener;
import com.remidiousE.exceptions.UserAlreadyExistsException;
import com.remidiousE.exceptions.UserNotFoundException;
import com.remidiousE.model.User;
import com.remidiousE.model.VerificationToken;
import com.remidiousE.repository.VerificationTokenRepository;
import com.remidiousE.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/register")
public class RegistrationController {

    private final UserService userService;

    private final ApplicationEventPublisher publisher;

    private final VerificationTokenRepository tokenRepository;

    private  final RegistrationCompleteEventListener eventListener;

    private final HttpServletRequest servletRequest;

    @PostMapping()
    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request) throws UserAlreadyExistsException {
        User user = userService.registerUser(registrationRequest);

        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return new ResponseEntity<>("Success! Please check your email to complete your registration", HttpStatus.CREATED);
    }

    @GetMapping("/verifyEmail")
    public String sendVerificationToken(@RequestParam("token") String token){
        String url = applicationUrl(servletRequest)+"/api/v1/register/resend-verification-token?token="+token;

        VerificationToken verifyToken = tokenRepository.findByToken(token);
        if(verifyToken.getUser().isEnabled()){
            return "This account has already been verified. please login";
        }
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")){
            return "Email verified successfully. Now you can login to your account";
        }
        return "Invalid verification link, <a href=\"" +url+"\"> Get a new verification link. </a>";
    }

    @GetMapping("/resend-verification-token")
    public String resendVerificationToken(@RequestParam("token") String oldToken, final HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        VerificationToken verificationToken = userService.generateNewVerificationToken(oldToken);
        User user = verificationToken.getUser();
        resendVerificationTokenEmail(user, applicationUrl(request), verificationToken);
        return "A new verification link has been sent to your email, " +
                "please check to activate your account";
    }

    private void resendVerificationTokenEmail(User user, String applicationUrl, VerificationToken verificationToken) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl+"/api/v1/register/verifyEmail?token="+verificationToken.getToken();
        eventListener.sendVerificationEmail(url);
        log.info("Click the link below to verify your registration : {}", url);
    }

    @PostMapping("/password-reset-request")
    public String resetPasswordRequest(@RequestBody PasswordRequest passwordResetRequest, final HttpServletRequest request) throws UserNotFoundException, MessagingException, UnsupportedEncodingException {
        Optional<User> user = userService.findByEmail(passwordResetRequest.getEmail());
        String passwordResetUrl = "";

        if (user.isPresent()){
            String passwordResetToken = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user.get(), passwordResetToken);
            passwordResetUrl = passwordResetEmailLink(user.get(), applicationUrl(request), passwordResetToken);
       }
        return passwordResetUrl;
    }

    private String passwordResetEmailLink(User user, String applicationUrl, String passwordResetToken) throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl+"/api/v1/register/reset-password?token="+passwordResetToken;
        eventListener.sendPasswordResetVerificationEmail(url);
        log.info("Click the link below to reset your password : {}", url);
        return url;
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody PasswordRequest passwordResetRequest, @RequestParam("token") String passwordRestToken){
        String tokenValidationResult = userService.validatePasswordResetToken(passwordRestToken);
        if (!tokenValidationResult.equalsIgnoreCase("valid")){
            return "Invalid password reset token";
        }
       Optional<User> user = Optional.ofNullable(userService.findUserByPasswordToken(passwordRestToken));
        if (user.isPresent()){
            userService.changePassword(user.get(), passwordResetRequest.getOldPassword());
            return "Password has been reset successfully";
        }
        return "Invalid password reset token";
    }

    @PostMapping("/change-password")
    public  String changePassword(@RequestBody PasswordRequest request) throws UserNotFoundException {
        User user = userService.findByEmail(request.getEmail()).get();
        if (!userService.oldPasswordIsValid(user, request.getOldPassword())){
            return "Incorrect password";
        }
        userService.changePassword(user, request.getOldPassword());
        return "Password has been changed successfully";
    }

    public String applicationUrl(HttpServletRequest request){
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}

