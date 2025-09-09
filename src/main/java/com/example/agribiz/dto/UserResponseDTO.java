package com.example.agribiz.dto;

import com.example.agribiz.Model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private String address;
    private String phoneNumber;
    private String nationalId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

