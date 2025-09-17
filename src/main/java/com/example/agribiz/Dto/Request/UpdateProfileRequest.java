package com.example.agribiz.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileRequest {
    private String firstName;
    private String lastName;
    private String nationalId;
    private String address;
    private String phoneNumber;
    private String bio;
}