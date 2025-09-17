package com.example.agribiz.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationResponse {
    private String message;
    private boolean success;
    private AuthenticationResponse authenticationResponse; // Only included on successful verification
}