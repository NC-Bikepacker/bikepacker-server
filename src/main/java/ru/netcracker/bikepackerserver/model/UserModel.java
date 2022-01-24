package ru.netcracker.bikepackerserver.model;

import ru.netcracker.bikepackerserver.entity.UserEntity;

import java.util.Objects;
import java.util.Optional;

public class UserModel {
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String userPicLink;

    public UserModel() {
    }

    public static UserModel toModel(UserEntity entity) {
        UserModel model = new UserModel();
        model.setFirstname(entity.getFirstname().orElse(null));
        model.setLastname(entity.getLastname().orElse(null));
        model.setUsername(entity.getUsername().orElse(null));
        model.setEmail(entity.getEmail().orElse(null));
        model.setUserPicLink(entity.getAvatarImageUrl().orElse(null));
        return model;
    }

    public Optional<String> getFirstname() {
        return Optional.ofNullable(firstname);
    }

    public Optional<String> getLastname() {
        return Optional.ofNullable(lastname);
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    public Optional<String> getUserPicLink() {
        return Optional.ofNullable(userPicLink);
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserPicLink(String userPicLink) {
        this.userPicLink = userPicLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserModel)) return false;
        UserModel userModel = (UserModel) o;
        return Objects.equals(getFirstname(), userModel.getFirstname()) && Objects.equals(getLastname(), userModel.getLastname()) && Objects.equals(getEmail(), userModel.getEmail()) && Objects.equals(getUsername(), userModel.getUsername()) && Objects.equals(getUserPicLink(), userModel.getUserPicLink());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstname(), getLastname(), getEmail(), getUsername(), getUserPicLink());
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + username + '\'' +
                ", userPicLink='" + userPicLink + '\'' +
                '}';
    }
}