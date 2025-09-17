package com.example.agribiz.Service.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final EmailService emailService;

    // In-memory storage for OTPs (consider using Redis for production)
    private final ConcurrentHashMap<String, OtpData> otpStorage = new ConcurrentHashMap<>();

    private static final int OTP_EXPIRY_MINUTES = 10;

    public void generateAndSendOtp(String email, String userFullName) {
        String otp = emailService.generateOTP();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES);

        // Store OTP with expiry time
        otpStorage.put(email, new OtpData(otp, expiryTime));

        log.info("Generated OTP for email: {}", email);

        // Create a temporary user object for email sending
        var tempUser = new com.example.agribiz.Model.User();
        tempUser.setEmail(email);
        tempUser.setFirstName(userFullName.split(" ")[0]);
        if (userFullName.split(" ").length > 1) {
            tempUser.setLastName(userFullName.substring(userFullName.indexOf(" ") + 1));
        }

        emailService.sendVerificationEmail(tempUser, otp);
    }

    public boolean verifyOtp(String email, String providedOtp) {
        OtpData otpData = otpStorage.get(email);

        if (otpData == null) {
            log.warn("No OTP found for email: {}", email);
            return false;
        }

        if (LocalDateTime.now().isAfter(otpData.getExpiryTime())) {
            log.warn("OTP expired for email: {}", email);
            otpStorage.remove(email); // Clean up expired OTP
            return false;
        }

        if (otpData.getOtp().equals(providedOtp)) {
            log.info("OTP verified successfully for email: {}", email);
            otpStorage.remove(email); // Clean up used OTP
            return true;
        }

        log.warn("Invalid OTP provided for email: {}", email);
        return false;
    }

    public void resendOtp(String email, String userFullName) {
        // Remove existing OTP if any
        otpStorage.remove(email);
        // Generate and send new OTP
        generateAndSendOtp(email, userFullName);
    }

    public void cleanupExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();
        otpStorage.entrySet().removeIf(entry -> now.isAfter(entry.getValue().getExpiryTime()));
    }

    // Inner class to store OTP data
    private static class OtpData {
        private final String otp;
        private final LocalDateTime expiryTime;

        public OtpData(String otp, LocalDateTime expiryTime) {
            this.otp = otp;
            this.expiryTime = expiryTime;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpiryTime() {
            return expiryTime;
        }
    }
}
