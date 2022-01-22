package ru.netcracker.bikepackerserver.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserProfileController {

    @GetMapping("/user")
    public String user(Model model) {
        return "<h1>Welcome, user!</h1>";
    }
}