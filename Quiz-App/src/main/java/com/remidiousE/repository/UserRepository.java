package com.remidiousE.repository;

import com.remidiousE.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findByEmail(String email);

    void deleteByUsername(String username);

    Optional<User> findByPassword(String password);
}
