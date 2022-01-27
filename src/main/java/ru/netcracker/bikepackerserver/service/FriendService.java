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

    public void addFriend(UserEntity user, Long id) throws NullPointerException {
        UserEntity friend = userRepository.getById(id);
        Friends friendRec = new Friends();
        if (friendRepository.existsByUserAndFriend(user, friend)) {
            throw new FriendAlreadyExistsException(user.getUsername(), friend.getUsername());
        }else {
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

    public void deleteFriend(UserEntity user, Long id) throws NullPointerException {
        Optional<UserEntity> friend = userRepository.findById(id);
        if (!friend.isPresent()) {
            throw new UserNotFoundException(friend.get().getId());
        }
        Friends fr1 = friendRepository.findByUserAndFriend(user, friend.get());
        if (friendRepository.existsByUserAndFriend(friend.get(), user)){
            updateStatus(friend.get(), user, false);
        }
        Long idFriendRec = fr1.getId();
        friendRepository.deleteById(idFriendRec);
    }

    public List<UserEntity> getFriends(UserEntity currentUser) {
        List<Friends> friendsByFirstUser = friendRepository.findByUserAndAccepted(currentUser, true);
        List<UserEntity> friendUsers = new ArrayList<>();
        for (Friends friend : friendsByFirstUser) {
            friendUsers.add(userRepository.getById(friend.getFriend().getId()));
        }
        return friendUsers;
    }

    public List<Friends> getAllFriends() {
        List<Friends> friendUsers = friendRepository.findAll();
        return friendUsers;
    }

    public void updateStatus(UserEntity user, UserEntity friend, boolean status){
        Friends friends = friendRepository.findByUserAndFriend(user, friend);

        if (friends != null){
            friendRepository.updateStatus(friends.getId(), status);
        }
    }

}