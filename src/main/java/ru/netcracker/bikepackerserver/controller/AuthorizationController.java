package ru.netcracker.bikepackerserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthorizationController {

    //    FIXME:
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }
}