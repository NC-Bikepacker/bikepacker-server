package ru.netcracker.bikepackerserver.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.repository.RoleRepo;
import ru.netcracker.bikepackerserver.repository.UserRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepo userRepository;
    @Autowired
    private RoleRepo rolesRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<UserEntity> user = Optional.ofNullable(userRepository.findByEmail(email));

        user.orElseThrow(() -> new UsernameNotFoundException(email + " not found."));

        return user.map(UserDetailsImpl::new).get();
    }

}
