package com.example.fitnessmanagementsystem.Controllers;

import com.example.fitnessmanagementsystem.Entities.User;
import com.example.fitnessmanagementsystem.Repositories.RoleRepository;
import com.example.fitnessmanagementsystem.Repositories.UserRepository;
import com.example.fitnessmanagementsystem.Services.RoleService;
import com.example.fitnessmanagementsystem.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Admin dashboard
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/dashboard";
    }

    // Admin login (handled by Spring Security)
    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    // Add a new member (User with Member role)
    @GetMapping("/add-member")
    public String showAddMemberForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/add-member";
    }

    //Admin can add a user to the db
    @PostMapping("/add-member")
    public String addMember(User user) {
        // Encode the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Add the "ROLE_MEMBER" role to the user
        // Assuming you have a method to assign roles to the user
        user.setRoles(Set.of(roleRepository.findByName("ROLE_MEMBER")));  // Assign "ROLE_MEMBER"

        // Save the user in the database
        userRepository.save(user);

        return "redirect:/admin/dashboard";  // Redirect to admin dashboard after adding the member
    }

    // Show all members to delete
    @GetMapping("/members-list")
    public String showMembers(Model model) {
        List<User> members = userService.getMembers();  // Retrieve all users from the database
        model.addAttribute("members", members);  // Add users to the model for display
        return "admin/members-list";  // Return the template to show the list
    }


    // Show delete member confirmation page (GET)
    @GetMapping("/delete-member/{id}")
    public String showDeleteMemberPage(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            return "admin/members-list";
        }
        model.addAttribute("member", user);
        return "admin/delete-member";  // Return the delete confirmation page
    }

    // Handle the deletion when the form is submitted (POST)
    @PostMapping("/delete-member/{id}")
    public String deleteMember(@PathVariable("id") Long id) {
        userService.deleteUser(id);  // Perform the deletion
        return "redirect:/admin/members-list";  // Redirect after successful deletion
    }

    // Add a new member (User with Member role)
    @GetMapping("/add-trainer")
    public String showAddTrainerForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/add-trainer";
    }

    //Add a trainer
    @PostMapping("/add-trainer")
    public String addTrainer(User user) {
        // Encode the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Add the "ROLE_MEMBER" role to the user
        // Assuming you have a method to assign roles to the user
        user.setRoles(Set.of(roleRepository.findByName("ROLE_TRAINER")));  // Assign "ROLE_MEMBER"

        // Save the user in the database
        userRepository.save(user);

        return "redirect:/admin/dashboard";  // Redirect to admin dashboard after adding the member
    }

    // Show all members to delete
    @GetMapping("/trainers-list")
    public String showAllTrainers(Model model) {
        List<User> trainers = userService.getTrainers();  // Retrieve all users from the database
        model.addAttribute("trainers", trainers);  // Add users to the model for display
        return "admin/trainers-list";  // Return the template to show the list
    }


    // Show delete member confirmation page (GET)
    @GetMapping("/delete-trainer/{id}")
    public String showDeleteTrainerPage(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            return "admin/trainers-list";
        }
        model.addAttribute("trainer", user);
        return "admin/delete-trainer";  // Return the delete confirmation page
    }

    // Handle the deletion when the form is submitted (POST)
    @PostMapping("/delete-trainer/{id}")
    public String deleteTrainer(@PathVariable("id") Long id) {
        userService.deleteUser(id);  // Perform the deletion
        return "redirect:/admin/trainers-list";  // Redirect after successful deletion
    }

    // Show all members in a list
    @GetMapping("/all-members-list")
    public String showAllMembers(Model model) {
        List<User> members = userService.getAllUsers(); // Fetch all members
        model.addAttribute("members", members); // Add them to the model for Thymeleaf to use
        return "admin/all-members-list"; // Thymeleaf template for listing members
    }

    // Show the edit form with the current member data
    @GetMapping("/edit-member/{id}")
    public String showEditMemberForm(@PathVariable("id") Long id, Model model) {
        User member = userService.getUserById(id);
        if (member != null) {
            model.addAttribute("member", member);
            return "admin/edit-member"; // Thymeleaf template for editing
        }
        return "redirect:/admin/all-members-list"; // Redirect if the member is not found
    }
    // Handle the form submission for updating the member data
    @PostMapping("/update-member/{id}")
    public String updateMember(@PathVariable("id") Long id, @ModelAttribute User updatedMember) {
        User existingMember = userService.getUserById(id);

        if (existingMember != null) {
            // Update member fields
            existingMember.setUsername(updatedMember.getUsername());
            existingMember.setEmail(updatedMember.getEmail());
            existingMember.setPassword(updatedMember.getPassword()); // Ideally, encrypt the password

            // Save the updated member
            userService.saveUser(existingMember);
        }

        return "redirect:/admin/all-members-list"; // Redirect to the members list after update
    }
}
