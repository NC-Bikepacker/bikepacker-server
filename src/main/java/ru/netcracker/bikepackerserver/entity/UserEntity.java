package ru.netcracker.bikepackerserver.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Entity
@Table( name = "users",
        schema = "public")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "first_name")
    private String firstname;

    @Column(name = "last_name")
    private String lastname;

    @Column(name = "nickname")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "avatar_image_url")
    private String avatarImageUrl;

    @ManyToOne()
    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    private RoleEntity roles;

    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<TrackEntity> tracks;

    public UserEntity() {
    }

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    public Optional<String> getFirstname() {
        return Optional.ofNullable(firstname);
    }

    public Optional<String> getLastname() {
        return Optional.ofNullable(lastname);
    }

    public Optional<String> getUsername() {
        return Optional.ofNullable(username);
    }

    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    public Optional<String> getAvatarImageUrl() {
        return Optional.ofNullable(avatarImageUrl);
    }

    public Optional<RoleEntity> getRoles() {
        return Optional.ofNullable(roles);
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public Set<TrackEntity> getTracks() {
        return tracks;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAvatarImageUrl(String avatarImageUrl) {
        this.avatarImageUrl = avatarImageUrl;
    }

    public void setRoles(RoleEntity roles) {
        this.roles = roles;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTracks(Set<TrackEntity> tracks) {
        this.tracks = tracks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getFirstname(), that.getFirstname()) && Objects.equals(getLastname(), that.getLastname()) && Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getPassword(), that.getPassword()) && Objects.equals(getAvatarImageUrl(), that.getAvatarImageUrl()) && Objects.equals(getRoles(), that.getRoles()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getTracks(), that.getTracks());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstname(), getLastname(), getUsername(), getPassword(), getAvatarImageUrl(), getRoles(), getEmail(), getTracks());
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", username='" + username + '\'' +
                ", avatarImageUrl='" + avatarImageUrl + '\'' +
                ", roles=" + roles +
                ", email='" + email + '\'' +
                ", tracks=" + tracks +
                '}';
    }
}
