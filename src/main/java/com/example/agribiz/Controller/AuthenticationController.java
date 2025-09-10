package com.example.agribiz.Controller;

import com.example.agribiz.Dto.ApiResponse;
import com.example.agribiz.Dto.*;
import com.example.agribiz.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        log.info("Registration request received for email: {}", request.getEmail());

        AuthenticationResponse response = userService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<AuthenticationResponse>builder()
                        .success(true)
                        .message("User registered successfully")
                        .data(response)
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(
            @Valid @RequestBody LoginRequest request
    ) {
        log.info("Authentication request received for email: {}", request.getEmail());

        AuthenticationResponse response = userService.authenticate(request);

        return ResponseEntity.ok(
                ApiResponse.<AuthenticationResponse>builder()
                        .success(true)
                        .message("User authenticated successfully")
                        .data(response)
                        .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {


        // In a stateless JWT implementation, logout is typically handled client-side
        // by removing the token. However, you can implement token blacklisting here if needed.

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("User logged out successfully")
                        .data("Logout successful")
                        .build());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(
            @Valid @RequestBody PasswordResetRequest request
    ) {
        log.info("Password reset request received for email: {}", request.getEmail());

        userService.requestPasswordReset(request);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Password reset email sent successfully")
                        .data("Check your email for password reset instructions")
                        .build());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(
            @Valid @RequestBody PasswordResetConfirm request
    ) {
        log.info("Password reset confirmation received");

        userService.confirmPasswordReset(request);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Password reset successfully")
                        .data("Your password has been updated")
                        .build());
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {
        log.info("Password change request received for user: {}", authentication.getName());

        userService.changePassword(authentication.getName(), request);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Password changed successfully")
                        .data("Your password has been updated")
                        .build());
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserInfo>> getProfile(Authentication authentication) {
        log.info("Profile request received for user: {}", authentication.getName());

        UserInfo userInfo = userService.getUserProfile(authentication.getName());

        return ResponseEntity.ok(
                ApiResponse.<UserInfo>builder()
                        .success(true)
                        .message("Profile retrieved successfully")
                        .data(userInfo)
                        .build());
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserInfo>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication
    ) {
        log.info("Profile update request received for user: {}", authentication.getName());

        UserInfo updatedProfile = userService.updateProfile(authentication.getName(), request);

        return ResponseEntity.ok(
                ApiResponse.<UserInfo>builder()
                        .success(true)
                        .message("Profile updated successfully")
                        .data(updatedProfile)
                        .build());
    }

    @PostMapping("/profile/image")
    public ResponseEntity<ApiResponse<String>> updateProfileImage(
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) {
        log.info("Profile image update request received for user: {}", authentication.getName());

        String imageUrl = userService.updateProfileImage(authentication.getName(), file);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Profile image updated successfully")
                        .data(imageUrl)
                        .build());
    }
}
