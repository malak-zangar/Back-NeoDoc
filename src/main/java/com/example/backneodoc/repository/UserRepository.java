package com.example.backneodoc.repository;

import com.example.backneodoc.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    User findByEmail(String email) ;

    User findByToken(String token);

    List<User> findAllByEnabled(Boolean enabled, Sort by);

    boolean existsByPassword(String password);
}