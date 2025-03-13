package com.example.fitnessmanagementsystem.Services;

import com.example.fitnessmanagementsystem.Entities.Shift;
import com.example.fitnessmanagementsystem.Entities.User;
import com.example.fitnessmanagementsystem.Repositories.ShiftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShiftService {

    @Autowired
    private ShiftRepository shiftRepository;

    @Autowired
    private UserService userService;  // Assuming userService to get trainers

    // Retrieve the trainer by ID and create a shift if the trainer exists
    public Shift createShift(Long trainerId, LocalDateTime startTime, LocalDateTime endTime, String dayOfWeek) {
        User trainer = userService.getUserById(trainerId);
        if (trainer != null) {
            Shift shift = new Shift(startTime, endTime, dayOfWeek, trainer);
            return (Shift) shiftRepository.save(shift);
        }
        return null;  // If no trainer found
    }

    // Method to get all shifts for a trainer
    public List<Shift> getShiftsByTrainer(Long trainerId) {
        User trainer = userService.getUserById(trainerId);
        if (trainer != null) {
            return shiftRepository.findByTrainer(trainer);  // Custom query to find shifts by trainer
        }
        return null;
    }

    // Method to get all shifts
    public List<Shift> getAllShifts() {
        return shiftRepository.findAll();
    }

    // Method to save a shift to the db
    public void saveShift(Shift shift) {
        // Log shift details to verify data before saving
        System.out.println("Saving shift: " + shift);

        // Ensure the shift is not null and contains valid values
        if (shift != null && shift.getTrainer() != null && shift.getStartTime() != null && shift.getEndTime() != null) {
            shiftRepository.save(shift);
        } else {
            throw new IllegalArgumentException("Invalid shift data");
        }
    }

    //Get Shift by ID
    public Shift getShiftById(Long id) {
        return shiftRepository.findById(id).orElse(null);  // Safely returns null if not found
    }

}
