package com.example.fitnessmanagementsystem.Repositories;

import com.example.fitnessmanagementsystem.Entities.Shift;
import com.example.fitnessmanagementsystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    List<Shift> findByTrainer(User trainer);

    List<Shift> findByTrainerId(Long trainerId);
}
