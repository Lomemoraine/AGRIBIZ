package com.example.agribiz.service;

import com.example.agribiz.Model.UserRole;
import com.example.agribiz.Model.users;
import com.example.agribiz.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public users createUser(users user) {
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists"); // We'll improve error handling later
        }
        return userRepository.save(user);
    }

    public List<users> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<users> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<users> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<users> getUsersByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    public users updateUser(Long id, users userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(userDetails.getUsername());
                    user.setEmail(userDetails.getEmail());
                    user.setPassword(userDetails.getPassword()); // We'll improve this later
                    user.setRole(userDetails.getRole());
                    user.setAddress(userDetails.getAddress());
                    user.setPhoneNumber(userDetails.getPhoneNumber());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
