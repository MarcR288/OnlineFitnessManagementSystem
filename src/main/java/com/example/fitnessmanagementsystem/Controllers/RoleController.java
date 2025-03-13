package com.example.fitnessmanagementsystem.Controllers;


import com.example.fitnessmanagementsystem.Services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoleController {

    @Autowired
    private RoleService roleService;


    @GetMapping("/roles")
    public String getRoles(Model model) {
        model.addAttribute("roles", roleService.getAllRoles());
        return "roles"; // returns a view called "roles.html"
    }
}
