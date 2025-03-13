package com.example.fitnessmanagementsystem.Controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    //GET for home.html
    @GetMapping("/home")
    public String home() {
        return "home";  // This will render the home.html template
    }
}
