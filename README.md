# Fitness Management API
This project is a Fitness Management RESTful API built with Spring Boot, leveraging Spring Security for authentication and authorization, and MySQL JDBC for data storage. It allows managing users, trainer schedules, roles, and attendance records within a fitness center. The application supports the following roles: Admin, Trainer, and User.

Features
Admin:
Create, read, update, and delete user accounts, admin accounts, and trainer accounts.
Manage trainer shifts and attendance records.

Trainer:
Login to view their scheduled shifts.
View and manage their attendance records.

User:
Login to view the available trainer schedules.
View and manage their attendance records.

Technologies Used
Spring Boot: Core framework for building the RESTful API.
Spring Security: Used to handle authentication and authorization, ensuring secure access to resources.
MySQL JDBC: Data storage for entities like Users, Attendance, Roles, and Trainer Shifts.
JWT (JSON Web Token): Used for secure, stateless authentication and authorization.
JPA (Java Persistence API): For object-relational mapping (ORM) with the MySQL database.
Lombok: Used to reduce boilerplate code (e.g., getters, setters, constructors) in Java classes.
Spring Boot Starter Test: For unit testing and integration testing the application.
Thymeleaf: Template engine used for rendering views in the web application.

Entities
User: Represents a user of the fitness center who can log in to view schedules and attendance.
Role: Defines different user roles: Admin, Trainer, and User. Each role has specific permissions within the system.
Shift: Stores information about trainer shifts, including trainer name, shift start time, end time, and date.
Attendance: Tracks attendance of both trainers and users for the fitness center activities.
Database Schema

The API interacts with the following database tables:
users: Stores user details including username, password, and role.
roles: Stores role definitions (Admin, User, Trainer).
trainer_shifts: Stores details about the shifts assigned to trainers.
attendance: Stores attendance logs for users and trainers.

Password Encryption with BCrypt
BCrypt is used to securely hash and encrypt user passwords before storing them in the database. This ensures that the passwords are never stored in plain text, adding an additional layer of security.
The password is hashed using the BCryptPasswordEncoder, which applies a salt and a hashing algorithm that makes it computationally expensive to perform brute force attacks on the password.


