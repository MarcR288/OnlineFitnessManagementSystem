package com.example.fitnessmanagementsystem.Controllers;


import com.example.fitnessmanagementsystem.Entities.Shift;
import com.example.fitnessmanagementsystem.Entities.User;
import com.example.fitnessmanagementsystem.Repositories.RoleRepository;
import com.example.fitnessmanagementsystem.Repositories.ShiftRepository;
import com.example.fitnessmanagementsystem.Repositories.UserRepository;
import com.example.fitnessmanagementsystem.Services.RoleService;
import com.example.fitnessmanagementsystem.Services.ShiftService;
import com.example.fitnessmanagementsystem.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ShiftRepository shiftRepository;
    @Autowired
    private ShiftService shiftService;

    //GET for Member Dashboard
    //Members can see the schedule and logout
    @GetMapping("/dashboard")
    public String memberDashboard(Model model, Authentication authentication) {
        // Get the current logged-in user (assumed to be a member)
        String username = authentication.getName();
        User user = userService.findByUsername(username); // Get the user based on their username

        // Fetch all shifts to show to the member
        List<Shift> shifts = shiftService.getAllShifts(); // Fetch all shifts (for members to see)

        // Add the shifts and username to the model
        model.addAttribute("shifts", shifts);
        model.addAttribute("username", username);
        model.addAttribute("user", user);

        return "member/dashboard"; // Return the dashboard view for members
    }


}
