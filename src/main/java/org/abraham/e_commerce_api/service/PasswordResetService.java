package org.abraham.e_commerce_api.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.abraham.e_commerce_api.entities.PasswordResetToken;
import org.abraham.e_commerce_api.repositories.PasswordResetRepository;
import org.abraham.e_commerce_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

// Example implementation
@Service
@AllArgsConstructor
public class PasswordResetService {
    private final PasswordResetRepository passwordResetRepository;
    private final PasswordEncoder passwordEncoder;
    private JavaMailSender mailSender;
    private final UserRepository userRepository;



    public void sendResetPasswordEmail(String email) throws MessagingException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = generateToken();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // ✅ No conversion needed
// Token valid for 24 hours
        passwordResetRepository.save(resetToken);

        String resetLink = "http://localhost:8080/auth/reset-password?token=" + token;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(email);
        helper.setSubject("Password Reset Request");
        helper.setText("Click the link below to reset your password:\n" + resetLink);
        mailSender.send(message);
    }
    // Method for generating token
    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }
        var user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword)); // Hash the new password before saving
        userRepository.save(user);
        passwordResetRepository.delete(resetToken);
    }
}