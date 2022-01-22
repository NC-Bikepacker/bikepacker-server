package ru.netcracker.bikepackerserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.netcracker.bikepackerserver.entity.UserEntity;

import javax.persistence.Column;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(@Param("nickname") String username);
    Optional<UserEntity> findById(@Param("user_id") Long id);
    Optional<UserEntity> findByEmail(String email);
}
