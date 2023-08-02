package com.remidiousE.utils;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailSender {
    @Resource
    private JavaMailSender javaMailSender;

    public void sendOtpEmail(String email, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Verify Otp");
        mimeMessageHelper.setText(
                "<div>" +
                        "<a href=\"http://localhost:8080/verify-account?email=%s&otp=%s\" target=\"_blank\">Click link to verify account</a>" +
                        "</div>".formatted(email, otp), true);

        javaMailSender.send(mimeMessage);
    }

    public void sendSetPasswordEmail(String email) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Set Password");
        mimeMessageHelper.setText(
                "<div>" +
                        "<a href=\"http://localhost:8080/set-password?email=%s\" target=\"_blank\">Click link to set password</a>" +
                        "</div>".formatted(email), true);

        javaMailSender.send(mimeMessage);
    }
}
