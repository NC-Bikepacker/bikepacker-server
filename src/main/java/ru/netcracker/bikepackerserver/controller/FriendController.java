package ru.netcracker.bikepackerserver.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.support.ObjectNameManager;
import org.springframework.web.bind.annotation.*;
import ru.netcracker.bikepackerserver.entity.Friends;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.repository.UserRepo;
import ru.netcracker.bikepackerserver.service.FriendService;
import ru.netcracker.bikepackerserver.service.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
    public ResponseEntity addFriend(@RequestBody String friendId, Principal principal) throws NullPointerException {
        UserEntity currentUser = userRepository.findByEmail(principal.getName());
        friendService.addFriend(currentUser, Long.parseLong(friendId));
        return ResponseEntity.ok("Friend added successfully");
    }

    @PostMapping(value = "/delete", consumes = "application/json", produces = "application/json")
    public ResponseEntity deleteFriend(Principal principal, @RequestBody String friendId) throws NullPointerException {
        UserEntity currentUser = userRepository.findByEmail(principal.getName());
        friendService.deleteFriend(currentUser, Long.parseLong(friendId));
        return ResponseEntity.ok("Friend deleted successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserEntity>> getFriends(Principal principal) {
        UserEntity currentUser = userRepository.findByEmail(principal.getName());
        List<UserEntity> myFriends = friendService.getFriends(currentUser);
        return new ResponseEntity<>(myFriends, HttpStatus.OK);
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
}
