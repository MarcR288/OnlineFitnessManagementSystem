package com.example.fitnessmanagementsystem.Controllers;

import com.example.fitnessmanagementsystem.Entities.Shift;
import com.example.fitnessmanagementsystem.Entities.User;
import com.example.fitnessmanagementsystem.Services.ShiftService;
import com.example.fitnessmanagementsystem.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ShiftController {

    @Autowired
    private ShiftService shiftService;

    @Autowired
    private UserService userService;

    //GET for trainer shifts
    @GetMapping("/admin/trainer-shifts")
    public String showAllShifts(Model model) {
        List<Shift> shifts = shiftService.getAllShifts();
        model.addAttribute("shifts", shifts);
        return "admin/trainer-shifts";
    }

    // Display shifts for a specific trainer
    @GetMapping("/trainer/{trainerId}/shifts")
    public String getTrainerShifts(@PathVariable("trainerId") Long trainerId, Model model) {
        List<Shift> shifts = shiftService.getShiftsByTrainer(trainerId);
        model.addAttribute("shifts", shifts);
        model.addAttribute("trainerId", trainerId);
        return "admin/trainer-shifts";  // Thymeleaf template for displaying shifts
    }

    // Handle form submission to create a shift
    @PostMapping("/trainer/{trainerId}/add-shift")
    public String addShift(@PathVariable("trainerId") Long trainerId,
                           @RequestParam("startTime") String startTimeStr,
                           @RequestParam("endTime") String endTimeStr,
                           @RequestParam("dayOfWeek") String dayOfWeek) {

        LocalDateTime startTime = LocalDateTime.parse(startTimeStr);  // Parse the start time
        LocalDateTime endTime = LocalDateTime.parse(endTimeStr);  // Parse the end time

        shiftService.createShift(trainerId, startTime, endTime, dayOfWeek);
        return "redirect:/admin/trainer/" + trainerId + "/shifts";  // Redirect to the list of shifts
    }

    //GET for adding a shift
    @GetMapping("/admin/add-shift")
    public String showAddShiftForm(Model model) {
        // Add any necessary attributes, such as a list of trainers if needed
        List<User> trainers = userService.getTrainers();
        model.addAttribute("trainers", trainers);
        model.addAttribute("shift", new Shift());  // An empty Shift object for form binding
        return "admin/add-shift";  // This should render the 'add-shift.html' page
    }

    //POST for adding a shift
    @PostMapping("/admin/add-shift")
    public String addShift(@RequestParam("trainerId") Long trainerId,
                           @RequestParam("startTime") String startTime,
                           @RequestParam("endTime") String endTime,
                           @RequestParam("dayOfWeek") String dayOfWeek,
                           Model model) {
        try {
            // Convert start and end time to LocalDateTime
            LocalDateTime start = LocalDateTime.parse(startTime);
            LocalDateTime end = LocalDateTime.parse(endTime);

            // Fetch the trainer by ID
            User trainer = userService.getTrainerById(trainerId);
            if (trainer == null) {
                model.addAttribute("error", "Trainer not found!");
                return "error";  // Show error page if trainer is not found
            }

            // Create and save the shift
            Shift shift = new Shift(start, end, dayOfWeek, trainer);
            shiftService.saveShift(shift);

            model.addAttribute("message", "Shift added successfully!");
            return "redirect:/admin/trainer-shifts";  // Redirect to the trainer's shifts page
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
            return "error"; // Show error page
        }
    }

    //GET for Show the edit form for a shift
    @GetMapping("/admin/edit-shift/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Shift shift = shiftService.getShiftById(id);

            // If the shift does not exist, show an error page
            if (shift == null) {
                model.addAttribute("error", "Shift not found!");
                return "error";  // Show error page if shift is not found
            }

            // Add shift and all trainers to the model
            model.addAttribute("shift", shift);
            model.addAttribute("trainers", userService.getTrainers());

            // Return the view for editing the shift
            return "admin/edit-shift";
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred.");
            return "error";
        }
    }

    //POST Handle the form submission for updating a shift
    @PostMapping("admin/edit-shift/{id}")
    public String updateShift(@PathVariable Long id,
                              @RequestParam("trainerId") Long trainerId,
                              @RequestParam("startTime") String startTime,
                              @RequestParam("endTime") String endTime,
                              @RequestParam("dayOfWeek") String dayOfWeek,
                              Model model) {
        try {
            // Parse the start and end times
            LocalDateTime start = LocalDateTime.parse(startTime);
            LocalDateTime end = LocalDateTime.parse(endTime);

            // Get the trainer by ID
            User trainer = userService.getTrainerById(trainerId);
            if (trainer == null) {
                model.addAttribute("error", "Trainer not found!");
                return "error";
            }

            // Get the existing shift and update its fields
            Shift shift = shiftService.getShiftById(id);
            if (shift == null) {
                model.addAttribute("error", "Shift not found!");
                return "error";
            }

            // Update shift properties
            shift.setTrainer(trainer);
            shift.setStartTime(start);
            shift.setEndTime(end);
            shift.setDayOfWeek(dayOfWeek);

            // Save the updated shift
            shiftService.saveShift(shift);

            model.addAttribute("message", "Shift updated successfully!");
            return "redirect:/admin/trainer-shifts";  // Redirect back to the list of shifts
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
            return "error";  // Show error page if any exception occurs
        }
    }
}
