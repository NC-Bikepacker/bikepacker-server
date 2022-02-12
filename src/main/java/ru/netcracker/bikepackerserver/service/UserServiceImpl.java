package ru.netcracker.bikepackerserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.exception.*;
import ru.netcracker.bikepackerserver.model.UserModel;
import ru.netcracker.bikepackerserver.repository.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
            throw new UsernameAlreadyExistsException(entity.getUsername());
        }

        if (userRepo.findByEmail(entity.getEmail()) != null) {
            throw new EmailAlreadyExistsException(entity.getEmail());
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
        Optional<List<UserEntity>> users = Optional.ofNullable(userRepo.findAll());

        if (users.isEmpty()) {
            throw new NoAnyUsersException();
        }

        List<UserModel> userModelList = new ArrayList<>();
        users.get().forEach(element -> userModelList.add(UserModel.toModel(element)));

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
        Optional<UserEntity> userEntity = Optional.ofNullable(userRepo.findById(id).get());

        if (userEntity.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        return UserModel.toModel(userEntity.get());
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
    public boolean update(UserModel newUser, Long id) throws UserNotFoundException {
        Optional<UserEntity> dbUser = userRepo.findById(id);

        if (dbUser.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        if (newUser.getFirstname().isPresent()) dbUser.get().setFirstname(newUser.getFirstname().get());
        if (newUser.getLastname().isPresent()) dbUser.get().setLastname(newUser.getLastname().get());
        if (newUser.getNickname().isPresent()) dbUser.get().setUserName(newUser.getNickname().get());
        if (newUser.getEmail().isPresent()) dbUser.get().setEmail(newUser.getEmail().get());
        if (newUser.getUserPicLink().isPresent()) dbUser.get().setAvatarImageUrl(newUser.getUserPicLink().get());

        return true;
    }

    /**
     * Delete user;
     *
     * @param id
     * @return true if user was deleted
     */
    @Override
    public boolean deleteById(Long id) throws UserNotFoundException {
        Optional<UserEntity> user = userRepo.findById(id);

        if (user.isPresent()) {
            userRepo.deleteById(id);
            return true;
        } else {
            throw new UserNotFoundException(id);
        }
    }

    /**
     * Delete all users;
     *
     * @return true if user was deleted and
     * false if didn't.
     */

    @Override
    public boolean deleteAll() throws UsersDeletingException {
        if (userRepo.count() != 0) {
            userRepo.deleteAll();
            return true;
        } else {
            throw new UsersDeletingException();
        }
    }

    public List<UserEntity> searchByFirstLastName(String name) {
        List<UserEntity> users = userRepo.findAll();
        List<UserEntity> res = new ArrayList<>();
        for (UserEntity user : users
        ) {
            if (contains(name, user.getFirstname()) || contains(name, user.getLastname()))
                res.add(user);
        }
        return res;
    }

    public UserModel searchByNickName(String nickName) {
        return UserModel.toModel(userRepo.findByUsername(nickName));
    }

    public boolean contains(String part, String user) {
        String REGEX_FIND_WORD = "(?i).*?" + part + ".*?";
        String regex = String.format(REGEX_FIND_WORD, Pattern.quote(part));
        if (user.matches(regex)){
            return true;
        }
        return false;
    }

}

}