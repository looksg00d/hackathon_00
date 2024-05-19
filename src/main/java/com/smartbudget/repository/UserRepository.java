package com.smartbudget.repository;

import com.smartbudget.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

    User findUserById(Long id);
}
