package com.example.fitnessmanagementsystem.Repositories;

import com.example.fitnessmanagementsystem.Entities.Attendance;
import com.example.fitnessmanagementsystem.Entities.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByShift(Shift shift);

    Attendance findByShiftId(Long shiftId);

    Attendance findByUserIdAndShiftId(Long userId, Long shiftId);
}
