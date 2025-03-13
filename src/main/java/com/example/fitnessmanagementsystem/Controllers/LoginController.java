package com.example.fitnessmanagementsystem.Controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    // Login Page
    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";  // This will render the login.html page
    }

    // After successful login, users will be redirected to their respective dashboards based on roles
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginRedirect(Authentication authentication) {
        // Get the current authenticated user
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = authority.getAuthority();

            if (role.equals("ROLE_ADMIN")) {
                return "redirect:/admin/dashboard";  // Admin Dashboard
            } else if (role.equals("ROLE_MEMBER")) {
                return "redirect:/member/dashboard"; // Member Dashboard
            } else if (role.equals("ROLE_TRAINER")) {
                return "redirect:/trainer/dashboard"; // Trainer Dashboard
            }
        }

        // Default redirect if no matching role is found
        return "redirect:/home"; // Redirect to home page or another default route
    }
}