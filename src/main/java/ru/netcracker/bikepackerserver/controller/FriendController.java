package ru.netcracker.bikepackerserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.bikepackerserver.data.FriendRequest;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.repository.FriendRepo;
import ru.netcracker.bikepackerserver.repository.UserRepo;
import ru.netcracker.bikepackerserver.service.FriendService;
import ru.netcracker.bikepackerserver.service.UserServiceImpl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/friends")
public class FriendController {
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private FriendService friendService;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping(value = "/add", consumes = "application/json", produces = "application/json")
    public ResponseEntity addFriend(@RequestBody FriendRequest request) throws NullPointerException {
        friendService.addFriend(request.getUserId(), request.getFriendId());
        return ResponseEntity.ok("Friend added successfully");
    }

    @DeleteMapping(value = "/delete", consumes = "application/json", produces = "application/json")
    public ResponseEntity deleteFriend(@RequestBody FriendRequest request) throws NullPointerException {
        friendService.deleteFriend(request.getUserId(), request.getFriendId());
        return ResponseEntity.ok("Friend deleted successfully");
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<UserEntity>> getFriends(@PathVariable Long userId) {
        List<UserEntity> myFriends = friendService.getFriends(userId);
        return new ResponseEntity<>(myFriends, HttpStatus.OK);
    }

    @GetMapping(value = "/search/{userId}/{name}")
    public ResponseEntity<List<UserEntity>> searchByName(@PathVariable Long userId, @PathVariable String name) {
        List<UserEntity> myFriends = friendService.getFriends(userId);
        List<UserEntity> res = new ArrayList<>();
        for (UserEntity user : myFriends
        ) {
            if (userService.contains(name, user.getFirstname()) || userService.contains(name, user.getLastname())) {
                res.add(user);
            }
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
