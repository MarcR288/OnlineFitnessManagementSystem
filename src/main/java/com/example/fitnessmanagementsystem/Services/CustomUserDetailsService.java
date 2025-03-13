package com.example.fitnessmanagementsystem.Services;

import com.example.fitnessmanagementsystem.Entities.User;
import com.example.fitnessmanagementsystem.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service // Ensure this annotation is here to make the class a Spring bean
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    /*
    The method loadUserByUsername is part of the UserDetailsService interface in Spring Security.
    This method is responsible for loading a user's details based on their username for
    authentication purposes
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve user from the database
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Convert roles (which are Role entities) to SimpleGrantedAuthority (which is what Spring Security needs)
        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))  // Extracts the role name (String) from Role and creates a SimpleGrantedAuthority
                .collect(Collectors.toSet());

        // Return Spring Security's User object with roles
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
