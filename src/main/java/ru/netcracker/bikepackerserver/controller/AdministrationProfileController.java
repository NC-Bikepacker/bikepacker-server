package ru.netcracker.bikepackerserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdministrationProfileController {
    @GetMapping("/admin")
    public String admin(Model model) {
        return "<h1>Welcome, admin!</h1>";
    }
}
