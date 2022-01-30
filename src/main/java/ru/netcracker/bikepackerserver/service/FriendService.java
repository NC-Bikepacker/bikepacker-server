package ru.netcracker.bikepackerserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.Friends;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.exception.FriendAlreadyExistsException;
import ru.netcracker.bikepackerserver.exception.UserNotFoundException;
import ru.netcracker.bikepackerserver.repository.FriendRepo;
import ru.netcracker.bikepackerserver.repository.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendService {

    @Autowired
    FriendRepo friendRepository;

    @Autowired
    UserRepo userRepository;

    public void addFriend(Long userId, Long friendId) throws NullPointerException {
        Optional<UserEntity> user = userRepository.findById(userId);
        Optional<UserEntity> friend = userRepository.findById(friendId);
        if (!user.isPresent()) {
            throw new UserNotFoundException(friend.get().getId());
        }
        if (!friend.isPresent()) {
            throw new UserNotFoundException(friend.get().getId());
        }
        Friends friendRec = new Friends();
        if (friendRepository.existsByUserAndFriend(user.get(), friend.get())) {
            throw new FriendAlreadyExistsException(user.get().getUsername(), friend.get().getUsername());
        } else {
            if (friendRepository.existsByUserAndFriend(friend.get(), user.get())) {
                updateStatus(friend.get(), user.get(), true);
                friendRec.setAccepted(true);
            } else {
                friendRec.setAccepted(false);
            }
            friendRec.setUser(user.get());
            friendRec.setFriend(friend.get());
            friendRepository.save(friendRec);
        }
    }

    public void deleteFriend(Long userId, Long friendId) throws NullPointerException {
        Optional<UserEntity> user = userRepository.findById(userId);
        Optional<UserEntity> friend = userRepository.findById(friendId);
        if (!user.isPresent()) {
            throw new UserNotFoundException(user.get().getId());
        }
        if (!friend.isPresent()) {
            throw new UserNotFoundException(friend.get().getId());
        }
        Friends fr1 = friendRepository.findByUserAndFriend(user.get(), friend.get());
        if (friendRepository.existsByUserAndFriend(friend.get(), user.get())) {
            updateStatus(friend.get(), user.get(), false);
        }
        Long idFriendRec = fr1.getId();
        friendRepository.deleteById(idFriendRec);
    }

    public List<UserEntity> getFriends(Long userId) {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new UserNotFoundException(user.get().getId());
        }
        List<Friends> friendsByFirstUser = friendRepository.findByUserAndAccepted(user.get(), true);
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