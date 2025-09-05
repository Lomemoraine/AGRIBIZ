package com.example.agribiz.controller;

import com.example.agribiz.Model.UserRole;
import com.example.agribiz.Model.User;
import com.example.agribiz.dto.UserRequestDTO;
import com.example.agribiz.dto.UserResponseDTO;
import com.example.agribiz.mapper.UserMapper;
import com.example.agribiz.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        User user = userMapper.toEntity(userRequestDTO);
        User createdUser = userService.createUser(user);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(user -> ResponseEntity.ok(userMapper.toResponseDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(user -> ResponseEntity.ok(userMapper.toResponseDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/role/{role}")
    public List<UserResponseDTO> getUsersByRole(@PathVariable UserRole role) {
        return userService.getUsersByRole(role).stream()
                .map(userMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        User user = userMapper.toEntity(userRequestDTO);
        User updatedUser = userService.updateUser(id, user);
        UserResponseDTO responseDTO = userMapper.toResponseDTO(updatedUser);
        return ResponseEntity.ok(responseDTO);
    }
//WORK ON THIS LATER WHEN IMPLEMENTING PASSWORD RESET
//    @PatchMapping("/{id}/password")
//    public ResponseEntity<?> changePassword(
//            @PathVariable Long id,
//            @RequestBody
//            @Size(min = 6)
//            String newPassword) {
//
//        userService.changePassword(id, newPassword);
//        return ResponseEntity.ok().body("Password updated successfully");
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().body("User deleted successfully");
    }

}
