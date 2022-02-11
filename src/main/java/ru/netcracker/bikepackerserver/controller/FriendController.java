package ru.netcracker.bikepackerserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.bikepackerserver.data.FriendRequest;
import ru.netcracker.bikepackerserver.entity.Friends;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.model.UserModel;
import ru.netcracker.bikepackerserver.repository.FriendRepo;
import ru.netcracker.bikepackerserver.repository.UserRepo;
import ru.netcracker.bikepackerserver.service.FriendService;
import ru.netcracker.bikepackerserver.service.UserServiceImpl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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
        return new ResponseEntity("Friend added successfully", HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete", consumes = "application/json", produces = "application/json")
    public ResponseEntity deleteFriend(@RequestBody FriendRequest request) throws NullPointerException {
        friendService.deleteFriend(request.getUserId(), request.getFriendId());
        return new ResponseEntity("Friend deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserEntity>> getFriends(Principal principal) {
        UserEntity currentUser = userRepository.findByEmail(principal.getName());
        List<UserEntity> myFriends = friendService.getFriends(currentUser);
        return new ResponseEntity<List<UserEntity>>(myFriends, HttpStatus.OK);
    }

    @GetMapping(value = "/search/{name}")
    public ResponseEntity<List<UserEntity>> searchByName(@PathVariable String name, Principal principal) {
        UserEntity currentUser = userRepository.findByEmail(principal.getName());
        List<UserEntity> myFriends = friendService.getFriends(currentUser);
        List<UserEntity> res = new ArrayList<>();
        for (UserEntity user : myFriends
        ) {
            if (userService.contains(name, user.getFirstname()) || userService.contains(name, user.getLastname())) {
                res.add(user);
            }
        }
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("{userName}")
    public ResponseEntity<List<UserModel>> getFriends(@PathVariable(name = "userName") String userNickName) {
        UserEntity currentUser = userRepository.findByUsername(userNickName);
        List<UserEntity> friends = friendService.getFriends(currentUser);
        return new ResponseEntity(friends, HttpStatus.OK);
    }
}