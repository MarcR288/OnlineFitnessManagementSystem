package com.example.fitnessmanagementsystem.Services;

import com.example.fitnessmanagementsystem.Entities.Role;
import com.example.fitnessmanagementsystem.Entities.User;
import com.example.fitnessmanagementsystem.Repositories.RoleRepository;
import com.example.fitnessmanagementsystem.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AdminUserService implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    //Creates an admin account if there are no admins in the db and sets the role to ROLE_ADMIN
    @Override
    public void run(String... args) throws Exception {
        // Check if there's already an admin user
        if (userRepository.findByUsername("admin") == null) {
            // Create the admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin")); // Encode password using BCrypt

            // Create roles
            Set<Role> roles = new HashSet<>();

            // Check if the "ADMIN" role exists
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");

            // If the "ADMIN" role doesn't exist, create it
            if (adminRole == null) {
                adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }

            // Add the "ADMIN" role to the user
            roles.add(adminRole);
            admin.setRoles(roles);

            // Save the admin user
            userRepository.save(admin);
        }
    }

}
