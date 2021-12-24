package ru.netcracker.bikepackerserver.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.netcracker.bikepackerserver.entity.UserEntity;

@Repository
public interface UserRepo extends CrudRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);
}
