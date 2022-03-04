package ru.netcracker.bikepackerserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netcracker.bikepackerserver.entity.UserEntity;

import java.io.Serializable;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long>, Serializable {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findById(String email);

    void deleteById(Long id);

    void deleteByUsername(String username);

    void deleteByEmail(String email);
}