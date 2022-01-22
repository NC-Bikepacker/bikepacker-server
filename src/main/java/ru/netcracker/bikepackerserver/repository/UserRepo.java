package ru.netcracker.bikepackerserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netcracker.bikepackerserver.entity.UserEntity;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);

    List<UserEntity> findByFirstname(String name);

    UserEntity findByEmail(String email);

//    @Transactional
//    @Modifying
//    @Query(value = "select from users where accepted = :accepted WHERE id = :id")
//    void updateStatus(@Param("id") Long id, @Param("accepted") boolean accepted);

}
