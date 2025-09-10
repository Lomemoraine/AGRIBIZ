package com.example.agribiz.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.agribiz.Model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByResetToken(String resetToken);

    @Query("SELECT u FROM User u WHERE u.resetToken = :token AND u.resetTokenExpiry > :now")
    Optional<User> findByValidResetToken(String token, LocalDateTime now);
}