package com.example.agribiz.Service;

import com.example.agribiz.Model.User;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.name}")
    private String appName;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendWelcomeEmail(User user) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject("Welcome to " + appName + "!");
            message.setText(buildWelcomeEmailContent(user));

            mailSender.send(message);
            log.info("Welcome email sent successfully to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send welcome email to: {}", user.getEmail(), e);
        }
    }

    public void sendPasswordResetEmail(User user, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(user.getEmail());
            message.setSubject("Password Reset Request - " + appName);
            message.setText(buildPasswordResetEmailContent(user, resetToken));

            mailSender.send(message);
            log.info("Password reset email sent successfully to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send password reset email to: {}", user.getEmail(), e);
        }
    }

    private String buildWelcomeEmailContent(User user) {
        return String.format(
                "Dear %s,\n\n" +
                        "Welcome to %s!\n\n" +
                        "Your account has been successfully created as a %s. " +
                        "You can now log in to your account and start connecting with other users in the potato farming community.\n\n" +
                        "Here's what you can do next:\n" +
                        "- Complete your profile with additional details\n" +
                        "- Upload a profile picture\n" +
                        "- Start exploring the platform\n\n" +
                        "If you have any questions or need assistance, please don't hesitate to contact our support team.\n\n" +
                        "Thank you for joining us!\n\n" +
                        "Best regards,\n" +
                        "The %s Team\n\n" +
                        "Visit us at: %s",
                user.getFullName(),
                appName,
                user.getRole().name().toLowerCase(),
                appName,
                frontendUrl
        );
    }

    private String buildPasswordResetEmailContent(User user, String resetToken) {
        String resetUrl = frontendUrl + "/reset-password?token=" + resetToken;

        return String.format(
                "Dear %s,\n\n" +
                        "We received a request to reset your password for your %s account.\n\n" +