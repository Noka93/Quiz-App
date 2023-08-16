package com.remidiousE.service;

import com.remidiousE.model.PasswordRestToken;
import com.remidiousE.model.User;
import com.remidiousE.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServices {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    public void createPasswordResetToken(User user, String passwordToken){
        PasswordRestToken passwordRestToken = new PasswordRestToken(passwordToken, user);
        passwordResetTokenRepository.save(passwordRestToken);
    }

    public String validatePasswordRestToken(String token){
            PasswordRestToken passwordToken = passwordResetTokenRepository.findByToken(token);
            if (passwordToken == null){
                return "Invalid password reset token";
            }
            User user = passwordToken.getUser();
            Calendar calendar = Calendar.getInstance();
            if ((passwordToken.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
                return "Link already expired, resend link";            }
            return "valid";
        }
    public Optional<User>findUserByPasswordToken(String passwordToken){
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(passwordToken).getUser());
    }
}

