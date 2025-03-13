package com.example.fitnessmanagementsystem.Services;

import com.example.fitnessmanagementsystem.Entities.Role;
import com.example.fitnessmanagementsystem.Repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // This method will create roles when the application starts
    @PostConstruct
    public void init() {
        // Create and save roles if not already in the database
        if (roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");

            Role trainerRole = new Role();
            trainerRole.setName("ROLE_TRAINER");

            Role memberRole = new Role();
            memberRole.setName("ROLE_MEMBER");


            roleRepository.saveAll(Arrays.asList(adminRole, trainerRole, memberRole));
        }
    }

    // Get all roles
    public List getAllRoles() {
        return roleRepository.findAll();
    }


}
