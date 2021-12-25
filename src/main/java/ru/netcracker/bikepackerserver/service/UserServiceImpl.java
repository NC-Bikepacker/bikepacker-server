package ru.netcracker.bikepackerserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.exceptions.EmailAlreadyExistsException;
import ru.netcracker.bikepackerserver.exceptions.NoAnyUsersException;
import ru.netcracker.bikepackerserver.exceptions.UserNotFoundException;
import ru.netcracker.bikepackerserver.exceptions.UsernameAlreadyExistsException;
import ru.netcracker.bikepackerserver.model.UserModel;
import ru.netcracker.bikepackerserver.repository.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    /**
     * Creates a new user.
     *
     * @param entity
     */
    @Override
    public UserModel create(UserEntity entity) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        if (userRepo.findByUsername(entity.getUsername()) != null) {
            throw new UsernameAlreadyExistsException("A user with this username already exists.");
        }

        if (userRepo.findByEmail(entity.getEmail()) != null) {
            throw new EmailAlreadyExistsException("This email address is already in use.");
        }

        return UserModel.toModel(userRepo.save(entity));
    }

    /**
     * Returns list of all users.
     *
     * @return list of all users.
     */
    @Override
    public List<UserModel> readAll() throws NoAnyUsersException {
        Iterable<UserEntity> users = userRepo.findAll();
        if (users.equals(null)) {
            throw new NoAnyUsersException("There are no any users.");
        }

        List<UserModel> userModelList = new ArrayList<>();
        users.forEach(element -> userModelList.add(UserModel.toModel(element)));

        return userModelList;
    }

    /**
     * Returns user by his uuid.
     *
     * @param id
     * @return user by his uuid.
     */
    @Override
    public UserModel read(Long id) throws UserNotFoundException {
        UserEntity userEntity = userRepo.findById(id).get();
        if (userEntity.equals(null)) {
            throw new UserNotFoundException("User with id=" + id + " not found.");
        }

        return UserModel.toModel(userEntity);
    }

    /**
     * Updates user's info finding him by uuid
     *
     * @param newUser
     * @param id
     * @return true if user was updated and
     * false if didn't.
     */
    @Override
    public boolean update(UserModel newUser, Long id) throws Exception {
        Optional<UserEntity> dbUser = userRepo.findById(id);
        if(dbUser.isPresent()) {
//            FIXME: Fix NullPointerException for UserModel fields
            if (!newUser.getFirstname().equals(null)) dbUser.get().setFirstname(newUser.getFirstname());
            if (!newUser.getLastname().equals(null)) dbUser.get().setLastname(newUser.getLastname());
            if (!newUser.getNickname().equals(null)) dbUser.get().setUserName(newUser.getNickname());
            if (!newUser.getEmail().equals(null)) dbUser.get().setEmail(newUser.getEmail());
            if (!newUser.getUserPicLink().equals(null)) dbUser.get().setAvatarImageUrl(newUser.getUserPicLink());
            return true;
        }
        return false;
    }

    /**
     * Delete user;
     *
     * @param id
     * @return true if user was deleted and
     * @return false if didn't.
     */
    @Override
    public boolean deleteById(Long id) throws Exception {
        if (!userRepo.findById(id).equals(null)) {
            userRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Delete all users;
     * @return true if user was deleted and
     *         false if didn't.
     */

    @Override
    public boolean deleteAll() throws Exception {
        if (userRepo.count() != 0) {
            userRepo.deleteAll();
            return true;
        }
        return false;
    }
}
