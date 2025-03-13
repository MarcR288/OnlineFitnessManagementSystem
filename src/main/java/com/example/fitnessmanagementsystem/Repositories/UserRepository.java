package com.example.fitnessmanagementsystem.Repositories;

import com.example.fitnessmanagementsystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findById(Long id);

    public List<User> findAll();

    public User findByUsername(String username);
}
