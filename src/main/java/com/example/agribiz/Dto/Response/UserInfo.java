package com.example.agribiz.Dto.Response;
import com.example.agribiz.Model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private String profileImageUrl;
    private String nationalId;
    private String address;
    private String phoneNumber;
    private String bio;
}