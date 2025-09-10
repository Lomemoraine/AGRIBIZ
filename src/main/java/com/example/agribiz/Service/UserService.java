package com.example.agribiz.Service;

import com.example.agribiz.Dto.*;
import com.example.agribiz.Model.User;
import com.example.agribiz.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final CloudinaryService cloudinaryService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

    public AuthenticationResponse register(RegisterRequest request) {
        log.info("Attempting to register user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email: " + request.getEmail());
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        var savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        // Send welcome email
        emailService.sendWelcomeEmail(savedUser);

        var jwtToken = jwtService.generateToken(user);
        var userInfo = mapToUserInfo(savedUser);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(userInfo)
                .build();
    }

    public AuthenticationResponse authenticate(LoginRequest request) {
        log.info("Attempting to authenticate user: {}", request.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        var jwtToken = jwtService.generateToken(user);
        var userInfo = mapToUserInfo(user);

        log.info("User authenticated successfully: {}", user.getEmail());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .user(userInfo)
                .build();
    }

    public UserInfo updateProfile(String userEmail, UpdateProfileRequest request) {
        log.info("Updating profile for user: {}", userEmail);

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getNationalId() != null) {
            user.setNationalId(request.getNationalId());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }

        var updatedUser = userRepository.save(user);
        log.info("Profile updated successfully for user: {}", userEmail);

        return mapToUserInfo(updatedUser);
    }

    public String updateProfileImage(String userEmail, MultipartFile file) {
        log.info("Updating profile image for user: {}", userEmail);

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        try {
            String imageUrl = cloudinaryService.uploadImage(file);
            user.setProfileImageUrl(imageUrl);
            userRepository.save(user);

            log.info("Profile image updated successfully for user: {}", userEmail);
            return imageUrl;
        } catch (Exception e) {
            log.error("Error updating profile image for user: {}", userEmail, e);
            throw new RuntimeException("Failed to upload image");
        }
    }

    public void changePassword(String userEmail, ChangePasswordRequest request) {
        log.info("Changing password for user: {}", userEmail);

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        log.info("Password changed successfully for user: {}", userEmail);
    }

    public void requestPasswordReset(PasswordResetRequest request) {
        log.info("Password reset requested for email: {}", request.getEmail());

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(24)); // Token valid for 24 hours

        userRepository.save(user);
        emailService.sendPasswordResetEmail(user, resetToken);

        log.info("Password reset email sent to: {}", request.getEmail());
    }

    public void confirmPasswordReset(PasswordResetConfirm request) {
        log.info("Confirming password reset for token: {}", request.getToken());

        var user = userRepository.findByValidResetToken(request.getToken(), LocalDateTime.now())
                .orElseThrow(() -> new InvalidTokenException("Invalid or expired reset token"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);

        log.info("Password reset confirmed for user: {}", user.getEmail());
    }

    public UserInfo getUserProfile(String userEmail) {
        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return mapToUserInfo(user);
    }

    private UserInfo mapToUserInfo(User user) {
        return UserInfo.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .profileImageUrl(user.getProfileImageUrl())
                .nationalId(user.getNationalId())
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .bio(user.getBio())
                .build();
    }
}