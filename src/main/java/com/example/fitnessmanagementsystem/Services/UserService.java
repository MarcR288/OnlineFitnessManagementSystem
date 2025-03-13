package com.example.fitnessmanagementsystem.Services;

import com.example.fitnessmanagementsystem.Entities.User;
import com.example.fitnessmanagementsystem.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }


    // Get all users and filter those who have the "TRAINER" role
    public List<User> getTrainers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals("ROLE_TRAINER")))
                .collect(Collectors.toList());
    }

    // Get all users and filter those who have the "MEMBER" role
    public List<User> getMembers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals("ROLE_MEMBER")))
                .collect(Collectors.toList());
    }

    //Gets user by id where the user's role is set to trainer
    public User getTrainerById(Long trainerId) {
        // Fetch the user by their ID
        Optional<User> user = userRepository.findById(trainerId);

        // Check if the user exists and if they have the "ROLE_TRAINER" role
        if (user.isPresent() && user.get().getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_TRAINER"))) {
            return user.get();  // Return the trainer if found
        }

        // Return null or throw an exception if the trainer is not found
        return null;  // Or throw an exception like new ResourceNotFoundException("Trainer not found")
    }

    //Find by username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
