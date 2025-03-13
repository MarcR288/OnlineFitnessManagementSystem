package com.example.fitnessmanagementsystem.Services;


import com.example.fitnessmanagementsystem.Entities.Attendance;
import com.example.fitnessmanagementsystem.Entities.Shift;
import com.example.fitnessmanagementsystem.Entities.User;
import com.example.fitnessmanagementsystem.Repositories.AttendanceRepository;
import com.example.fitnessmanagementsystem.Repositories.ShiftRepository;
import com.example.fitnessmanagementsystem.Repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AttendanceService {

    @Autowired
    AttendanceRepository attendanceRepository;

    @Autowired
    ShiftRepository shiftRepository;


    @Autowired
    private UserRepository userRepository;


    //Logic For Marking attendance
    @Transactional
    public void markAttendance(Long userId, Long shiftId, boolean isAttended) {
        // Fetch the existing User and Shift from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID " + userId));
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new EntityNotFoundException("Shift not found with ID " + shiftId));

        // Check if an attendance record already exists for this user and shift
        Attendance attendance = attendanceRepository.findByUserIdAndShiftId(userId, shiftId);

        if (attendance == null) {
            // If no existing attendance record, create a new one
            attendance = new Attendance();
            attendance.setUser(user);  // Set the fetched User entity
            attendance.setShift(shift);  // Set the fetched Shift entity
        }

        // Update the attendance status
        attendance.setAttended(isAttended);

        // Save the attendance record
        attendanceRepository.save(attendance);
    }
}
