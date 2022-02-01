package ru.netcracker.bikepackerserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.Friends;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.exception.FriendAlreadyExistsException;
import ru.netcracker.bikepackerserver.exception.UserNotFoundException;
import ru.netcracker.bikepackerserver.repository.FriendRepo;
import ru.netcracker.bikepackerserver.repository.UserRepo;

import java.util.ArrayList;
import java.util.List;

@Service
public class FriendService {

    FriendRepo friendRepository;

    UserRepo userRepository;

    @Autowired
    public FriendService(FriendRepo friendRepository, UserRepo userRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    public void addFriend(Long userId, Long friendId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        UserEntity friend = userRepository.findById(friendId).orElseThrow(() -> new UserNotFoundException(friendId));
        Friends friendRec = new Friends();
        if (friendRepository.existsByUserAndFriend(user, friend)) {
            throw new FriendAlreadyExistsException(user.getUsername(), friend.getUsername());
        } else {
            if (friendRepository.existsByUserAndFriend(friend, user)) {
                updateStatus(friend, user, true);
                friendRec.setAccepted(true);
            } else {
                friendRec.setAccepted(false);
            }
            friendRec.setUser(user);
            friendRec.setFriend(friend);
            friendRepository.save(friendRec);
        }
    }

    public void deleteFriend(Long userId, Long friendId) throws NullPointerException {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        UserEntity friend = userRepository.findById(friendId).orElseThrow(() -> new UserNotFoundException(friendId));
        Friends fr1 = friendRepository.findByUserAndFriend(user, friend);
        if (friendRepository.existsByUserAndFriend(friend, user)) {
            updateStatus(friend, user, false);
        }
        Long idFriendRec = fr1.getId();
        friendRepository.deleteById(idFriendRec);
    }

    public List<UserEntity> getFriends(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<Friends> friendsByFirstUser = friendRepository.findByUserAndAccepted(user, true);
        List<UserEntity> friendUsers = new ArrayList<>();
        for (Friends friend : friendsByFirstUser) {
            friendUsers.add(userRepository.getById(friend.getFriend().getId()));
        }
        return friendUsers;
    }

    public void updateStatus(UserEntity user, UserEntity friend, boolean status) {
        Friends friends = friendRepository.findByUserAndFriend(user, friend);
        if (friends != null) {
            friendRepository.updateStatus(friends.getId(), status);
        }
    }
}