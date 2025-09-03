package com.example.agribiz.controller;

import com.example.agribiz.Model.UserRole;
import com.example.agribiz.Model.User;
import com.example.agribiz.dto.UserRequestDTO;
import com.example.agribiz.dto.UserResponseDTO;
import com.example.agribiz.mapper.UserMapper;
import com.example.agribiz.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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

    @PatchMapping("/{id}/password")
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody String newPassword) {
        userService.changePassword(id, newPassword);
        return ResponseEntity.ok().body("Password updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().body("User deleted successfully");
    }

//    private final UserService userService;
//
//    @Autowired
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @PostMapping
//    public User createUser(@RequestBody User user) {
//        return userService.createUser(user);
//    }
//
//    @GetMapping
//    public List<User> getAllUsers() {
//        return userService.getAllUsers();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<User> getUserById(@PathVariable Long id) {
//        Optional<User> user = userService.getUserById(id);
//        return user.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @GetMapping("/email/{email}")
//    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
//        Optional<User> user = userService.getUserByEmail(email);
//        return user.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @GetMapping("/role/{role}")
//    public List<User> getUsersByRole(@PathVariable UserRole role) {
//        return userService.getUsersByRole(role);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
//        try {
//            User updatedUser = userService.updateUser(id, userDetails);
//            return ResponseEntity.ok(updatedUser);
//        } catch (RuntimeException e) {
//            if (e.getMessage().contains("not found")) {
//                return ResponseEntity.notFound().build();
//            }
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
//        boolean deleted = userService.deleteUser(id);
//        if (deleted) {
//            return ResponseEntity.ok().body("User deleted successfully");
//        }
//        return ResponseEntity.notFound().build();
//    }
}
