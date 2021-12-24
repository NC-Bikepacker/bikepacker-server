package ru.netcracker.bikepackerserver.model;

import ru.netcracker.bikepackerserver.model.enums.Role;

import java.util.Objects;

public class User {
    private String firstname;
    private String lastname;
    private String email;
    private Long id;
    private String nickname;
    private String userPicLink;
    private Role role;

    public User(String firstname, String lastname, String email, Long id, Role role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.id = id;
        this.role = role;
        nickname = firstname + "_" + lastname + "@" + id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserPicLink() {
        return userPicLink;
    }

    public void setUserPicLink(String userPicLink) {
        this.userPicLink = userPicLink;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof User)) return false;
//        User user = (User) o;
//        return getId() == user.getId() && getFirstname().equals(user.getFirstname()) && getLastname().equals(user.getLastname()) && Objects.equals(passHash, user.passHash) && getEmail().equals(user.getEmail()) && Objects.equals(getNickname(), user.getNickname()) && Objects.equals(getUserPicLink(), user.getUserPicLink()) && getRole() == user.getRole();
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getFirstname(), getLastname(), getEmail(), getId(), getNickname(), getUserPicLink(), getRole());
//    }
//
//    @Override
//    public String toString() {
//        return "User{" +
//                "firstname='" + firstname + '\'' +
//                ", lastname='" + lastname + '\'' +
//                ", email='" + email + '\'' +
//                ", id=" + id +
//                ", role=" + role +
//                '}';
//    }
}
