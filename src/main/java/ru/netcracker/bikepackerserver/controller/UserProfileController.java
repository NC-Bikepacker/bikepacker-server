package ru.netcracker.bikepackerserver.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netcracker.bikepackerserver.entity.UserEntity;

import java.security.Principal;

@RestController
public class UserProfileController {

    @GetMapping("/user")
    public String user(Model model, Principal principal) {
        return "<h1>Welcome, user " + principal.getName() + "<!</h1>>";
    }

    @PostMapping("/user1")
    public String addFriend1(Model model, Principal principal, @RequestBody UserEntity userEntity) {
        return "<h1>Welcome, user " + principal.getName() + "<!</h1>>";
    }
    @PostMapping("/user2")
    public String addFriend2(Model model, Principal principal, @RequestBody String emailFriend) {

        return "<h1>Welcome, user " + principal.getName() + "<!</h1>>";
    }
}
