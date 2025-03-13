package com.example.fitnessmanagementsystem.Controllers;

import com.example.fitnessmanagementsystem.Entities.Shift;
import com.example.fitnessmanagementsystem.Entities.User;
import com.example.fitnessmanagementsystem.Services.AttendanceService;
import com.example.fitnessmanagementsystem.Services.ShiftService;
import com.example.fitnessmanagementsystem.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/trainer")
public class TrainerController {

    @Autowired
    private UserService userService;

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private AttendanceService attendanceService;

    //GET for Trainer Dashboard
    @GetMapping("/dashboard")
    public String trainerDashboard(Model model, Authentication authentication) {
        // Get the current logged-in user (assumed to be a member)
        String username = authentication.getName();
        User user = userService.findByUsername(username); // Get the user based on their username

        // Fetch all shifts to show to the member
        List<Shift> shifts = shiftService.getAllShifts(); // Fetch all shifts (for members to see)

        // Add the shifts and username to the model
        model.addAttribute("shifts", shifts);
        model.addAttribute("username", username);
        model.addAttribute("user", user);

        return "trainer/dashboard"; // Return the dashboard view for members
    }

    //GET - Shows a list of members the trainer can mark present/absent
    @GetMapping("/mark-attendance/{shiftId}")
    public String showAttendanceForm(@PathVariable Long shiftId, Model model) {
        Shift shift = shiftService.getShiftById(shiftId);
        List<User> members = userService.getMembers();
        Map<Long, Boolean> attendance = new HashMap<>();

        // Initialize attendance map with default values (e.g., all users absent by default)
        for (User user : members) {
            attendance.put(user.getId(), false);  // You can initialize to false or some default state
        }

        model.addAttribute("shift", shift);
        model.addAttribute("members", members);  // Pass the users to the view
        model.addAttribute("attendance", attendance); // Add the attendance map to the model
        return "trainer/mark-attendance";  // Return the view where attendance can be marked
    }

    //POST for marking attendance.
    @PostMapping("/mark-attendance/{shiftId}")
    public ResponseEntity<Map<Long, Boolean>> markAttendance(@PathVariable("shiftId") Long shiftId,
                                                             @RequestBody Map<Long, Boolean> attendance) {

        // Process the attendance map
        for (Map.Entry<Long, Boolean> entry : attendance.entrySet()) {
            Long userId = entry.getKey();
            boolean attended = entry.getValue();

            // Save the attendance for the user in the given shift
            attendanceService.markAttendance(userId, shiftId, attended);
        }

        // Optionally return some response to confirm the operation
        return ResponseEntity.ok(attendance);  // You could return the updated attendance data
    }
}
