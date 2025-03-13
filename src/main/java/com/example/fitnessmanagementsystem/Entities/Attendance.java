package com.example.fitnessmanagementsystem.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Attendance {

    //Using Lombok for getters/setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shift_id", nullable = false)
    @Setter
    @Getter
    private Shift shift;

    @Setter
    @Getter
    private Boolean attended; // True or false if the trainer marked the attendance

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // Assuming this is how you are mapping it
    @Setter
    @Getter
    private User user;

    // Constructors, getters, and setters
    public Attendance() {}

    public Attendance(Shift shift, Boolean attended) {
        this.shift = shift;
        this.attended = attended;
    }




    // Getters and setters...
}
