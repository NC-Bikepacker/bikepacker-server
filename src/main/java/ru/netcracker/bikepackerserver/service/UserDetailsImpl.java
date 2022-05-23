package ru.netcracker.bikepackerserver.service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.netcracker.bikepackerserver.entity.RoleEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String firstname;
    private String lastname;
    private String nickname;
    private String password;
    private String avatarImageUrl;
    private String email;
    private RoleEntity roles;
    private List<GrantedAuthority> authorities;
    private Long id;
    private boolean verification;

    public UserDetailsImpl(UserEntity user) {
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.nickname = user.getUsername();
        this.password = user.getPassword();
        this.avatarImageUrl = user.getAvatarImageUrl();
        this.email = user.getEmail();
        this.roles = user.getRoles();
        this.id = user.getId();
        this.authorities = Arrays.stream(user.getRoles().getRoleName().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        this.verification = user.isAccountVerification();
    }

    public UserDetailsImpl() {
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return nickname;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarImageUrl() {
        return avatarImageUrl;
    }

    public void setAvatarImageUrl(String avatarImageUrl) {
        this.avatarImageUrl = avatarImageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleEntity getRoles() {
        return roles;
    }

    public void setRoles(RoleEntity roles) {
        this.roles = roles;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isVerification() {
        return verification;
    }

    public void setVerification(boolean verification) {
        this.verification = verification;
    }

    @Override
    public String toString() {
        return "UserDetailsImpl{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", avatarImageUrl='" + avatarImageUrl + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", authorities=" + authorities +
                ", id=" + id +
                ", verification=" + verification +
                '}';
    }
}
