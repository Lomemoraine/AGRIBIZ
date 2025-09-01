package com.example.agribiz.repository;


import com.example.agribiz.Model.UserRole;
import com.example.agribiz.Model.users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<users, Long> {
    Optional<users> findByEmail(String email);

    List<users> findByRole(UserRole role);

    boolean existsByEmail(String email);

}