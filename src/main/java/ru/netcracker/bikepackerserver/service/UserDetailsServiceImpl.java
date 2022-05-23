package ru.netcracker.bikepackerserver.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.repository.RoleRepo;
import ru.netcracker.bikepackerserver.repository.UserRepo;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl  implements UserDetailsService {

    private UserRepo userRepository;
    private RoleRepo rolesRepository;

    public UserDetailsServiceImpl(UserRepo userRepository, RoleRepo rolesRepository) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> user = Optional.ofNullable(userRepository.findByEmail(email).get());
        user.orElseThrow(() -> new UsernameNotFoundException(email + " not found."));
        return user.map(UserDetailsImpl::new).get();
    }
}
