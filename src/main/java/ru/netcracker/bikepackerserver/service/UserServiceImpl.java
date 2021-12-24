package ru.netcracker.bikepackerserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.exceptions.EmailAlreadyExistsException;
import ru.netcracker.bikepackerserver.exceptions.NoAnyUsersException;
import ru.netcracker.bikepackerserver.exceptions.UserNotFoundException;
import ru.netcracker.bikepackerserver.exceptions.UsernameAlreadyExistsException;
import ru.netcracker.bikepackerserver.model.User;
import ru.netcracker.bikepackerserver.repository.UserRepo;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    /**
     * Creates a new user.
     *
     * @param user
     */
    @Override
    public UserEntity create(UserEntity user) throws UsernameAlreadyExistsException, EmailAlreadyExistsException {
        if (userRepo.findByUsername(user.getUsername()) != null) {
            throw new UsernameAlreadyExistsException("A user with this username already exists.");
        }

        if (userRepo.findByEmail(user.getEmail()) != null) {
            throw new EmailAlreadyExistsException("This email address is already in use.");
        }

//        TODO: Check if user's role id is correct

        return userRepo.save(user);
    }

    /**
     * Returns list of all users.
     *
     * @return list of all users.
     */
    @Override
    public Iterable<UserEntity> readAll() throws NoAnyUsersException {
        Iterable<UserEntity> users = userRepo.findAll();
        if (users.equals(null)) {
            throw new NoAnyUsersException("There are no any users.");
        }
        return users;
    }

    /**
     * Returns user by his uuid.
     *
     * @param id
     * @return user by his uuid.
     */
    @Override
    public UserEntity read(Long id) throws UserNotFoundException {
        UserEntity userEntity = userRepo.findById(id).get();
        if (userEntity.equals(null)) {
            throw new UserNotFoundException("User with id=" + id + " not found.");
        }

        return userEntity;
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
    public boolean update(User newUser, Long id) {
        try {
            UserEntity dbUser = userRepo.findById(id).get();
            dbUser.setFirstname(newUser.getFirstname());
            dbUser.setLastname(newUser.getLastname());
            dbUser.setUserName(newUser.getNickname());
            dbUser.setRoleId(newUser.getRole().getId());
            dbUser.setEmail(newUser.getEmail());
//            dbUser.get().setAvatarImageUrl(newUser.getUserPicLink());
//            FIXME: Check & fix this method
            userRepo.save(dbUser);
            return true;
        } catch (Exception e) {
            return false;
        }
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
     *
     * @return true if users were deleted and
     * @return false if weren't.
     */
    public boolean deleteAll() throws Exception {
        userRepo.deleteAll();
        return true;
    }
}
