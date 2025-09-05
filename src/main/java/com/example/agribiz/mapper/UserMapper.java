package com.example.agribiz.mapper;

import com.example.agribiz.Model.User;
import com.example.agribiz.dto.UserRequestDTO;
import com.example.agribiz.dto.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toEntity(@Valid UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(userRequestDTO.getPassword()); // Will be encrypted in service
        user.setRole(userRequestDTO.getRole());
        user.setAddress(userRequestDTO.getAddress());
        user.setPhoneNumber(userRequestDTO.getPhoneNumber());
        user.setNationalId(userRequestDTO.getNationalId());
        return user;
    }

    public UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setUsername(user.getUsername());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setRole(user.getRole());
        responseDTO.setAddress(user.getAddress());
        responseDTO.setPhoneNumber(user.getPhoneNumber());
        responseDTO.setCreatedAt(user.getCreatedAt());
        responseDTO.setUpdatedAt(user.getUpdatedAt());
        responseDTO.setNationalId(user.getNationalId());
        return responseDTO;
    }
}
