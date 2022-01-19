package ru.netcracker.bikepackerserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.bikepackerserver.entity.Friends;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.repository.UserRepo;
import ru.netcracker.bikepackerserver.service.FriendService;

import java.security.Principal;
import java.util.List;
@RestController
@RequestMapping("/friends")
public class FriendController {
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private FriendService friendService;

    @PostMapping(value = "/test", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> test(@RequestBody String friendId, Principal principal) throws NullPointerException{

        return new ResponseEntity(friendId, HttpStatus.OK);
    }

    @PostMapping(value = "/add", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addFriend(@RequestBody String friendId, Principal principal) throws NullPointerException{
        UserEntity currentUser = userRepository.findByEmail(principal.getName());
        friendService.addFriend(currentUser,Long.parseLong(friendId));
        return ResponseEntity.ok("Friend added successfully");
    }

    @PostMapping(value = "/delete", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteFriend(@RequestBody String friendId, Principal principal) throws NullPointerException{
        UserEntity currentUser = userRepository.findByEmail(principal.getName());
        friendService.deleteFriend(currentUser,Long.parseLong(friendId));
        return ResponseEntity.ok("Friend deleted successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserEntity>> getFriends(Principal principal) {
        UserEntity currentUser = userRepository.findByEmail(principal.getName());
        List<UserEntity> myFriends = friendService.getFriends(currentUser);
        return new ResponseEntity<List<UserEntity>>(myFriends, HttpStatus.OK);
    }

    @GetMapping("/allrecords")
    public ResponseEntity<List<Friends>> allRecords() {
//        UserEntity currentUser = userRepository.findByEmail(principal.getName());
        List<Friends> myFriends = friendService.getAllFriends();
        return new ResponseEntity<List<Friends>>(myFriends, HttpStatus.OK);
    }


}
