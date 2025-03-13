package com.example.fitnessmanagementsystem.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
public class SecurityConfig {

    // Autowire the UserDetailsService (CustomUserDetailsService will be injected here)
    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;

    // Define the password encoder bean
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configure HTTP security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/trainer/mark-attendance/**")  // Exclude specific endpoint from CSRF protection
                )
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/admin/delete-member/**").hasAuthority("ROLE_ADMIN")  // Allow only admins to delete members
                                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/member/**").hasAuthority("ROLE_MEMBER")
                                .requestMatchers("/trainer/**").hasAuthority("ROLE_TRAINER")
                                .requestMatchers("/", "/home", "/register").permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")  // Custom login page URL
                        .loginProcessingUrl("/login")  // URL to process the login
                        .successHandler(authenticationSuccessHandler())  // Use the custom success handler
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // URL to trigger logout
                        .logoutSuccessUrl("/login?logout")  // Redirect after logout
                        .invalidateHttpSession(true) // Invalidate session
                        .deleteCookies("JSESSIONID") // Delete session cookie
                        .permitAll()
                );
        return http.build();
    }

    // Configure authentication manager with custom UserDetailsService and PasswordEncoder
    @Autowired
    public void configure(AuthenticationManagerBuilder auth,
                          @Lazy UserDetailsService userDetailsService,
                          @Lazy PasswordEncoder passwordEncoder) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    // Define AuthenticationSuccessHandler based on users' role.
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            try {
                // Get the user's roles and redirect based on them
                authentication.getAuthorities().forEach(authority -> {
                    String role = authority.getAuthority();
                    try {
                        if ("ROLE_ADMIN".equals(role)) {
                            System.out.println("Redirecting admin to /admin/dashboard");  // Log for debugging
                            response.sendRedirect("/admin/dashboard");
                        } else if ("ROLE_MEMBER".equals(role)) {
                            System.out.println("Redirecting member to /member/dashboard");
                            response.sendRedirect("/member/dashboard");
                        } else if ("ROLE_TRAINER".equals(role)) {
                            System.out.println("Redirecting trainer to /trainer/dashboard");
                            response.sendRedirect("/trainer/dashboard");
                        } else {
                            System.out.println("Redirecting to /home");
                            response.sendRedirect("/home");
                        }
                    } catch (IOException e) {
                        System.err.println("Redirection failed for role: " + role);
                        e.printStackTrace();  // Print stack trace to console or logs
                        throw new RuntimeException("Redirection failed", e);
                    }
                });
            } catch (Exception e) {
                System.err.println("Error during authentication success handler");
                e.printStackTrace();
                throw new RuntimeException("Error during authentication success handler", e);
            }
        };
    }
}
