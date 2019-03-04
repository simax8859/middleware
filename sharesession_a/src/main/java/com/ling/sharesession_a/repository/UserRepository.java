package com.ling.sharesession_a.repository;


import com.ling.sharesession_a.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
}