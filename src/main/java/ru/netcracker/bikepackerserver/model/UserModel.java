package ru.netcracker.bikepackerserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.validation.annotation.Validated;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.service.UserDetailsImpl;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Validated
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserModel {

    @NotEmpty(message = "Firstname can not be empty")
    private String firstname;

    @NotEmpty(message = "Lastname can not be empty")
    private String lastname;

    @NotEmpty(message = "Email can not be empty")
    @Email(message = "Email is not valid")
    private String email;

    @NotEmpty(message = "Username can not be empty")
    private String username;

    @NotNull(message = "Userpic URL can not be null")
    private String userPicLink;

    @NotNull
    private Long id;

    public UserModel() {
    }

    public static UserModel toModel(UserEntity entity) {
        UserModel model = new UserModel();
        model.setFirstname(entity.getFirstname());
        model.setLastname(entity.getLastname());
        model.setUsername(entity.getUsername());
        model.setEmail(entity.getEmail());
        model.setUserPicLink(entity.getAvatarImageUrl());
        model.setId(entity.getId());
        return model;
    }

    public static UserModel toModel(UserDetailsImpl userDetails) {
        UserModel model = new UserModel();
        model.setFirstname(userDetails.getFirstname());
        model.setLastname(userDetails.getLastname());
        model.setUsername(userDetails.getUsername());
        model.setEmail(userDetails.getEmail());
        model.setUserPicLink(userDetails.getAvatarImageUrl());
        model.setId(userDetails.getId());
        return model;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getUserPicLink() {
        return userPicLink;
    }

    public Long getId() {
        return id;
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

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserModel)) return false;
        UserModel userModel = (UserModel) o;
        return Objects.equals(getFirstname(), userModel.getFirstname()) && Objects.equals(getLastname(), userModel.getLastname()) && Objects.equals(getEmail(), userModel.getEmail()) && Objects.equals(getUsername(), userModel.getUsername()) && Objects.equals(getUserPicLink(), userModel.getUserPicLink()) && Objects.equals(getId(), userModel.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstname(), getLastname(), getEmail(), getUsername(), getUserPicLink(), getId());
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", userPicLink='" + userPicLink + '\'' +
                ", id=" + id +
                '}';
    }
}