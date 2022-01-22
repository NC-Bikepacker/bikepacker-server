package ru.netcracker.bikepackerserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.netcracker.bikepackerserver.entity.Friends;
import ru.netcracker.bikepackerserver.entity.UserEntity;

import java.util.List;

@Repository
public interface FriendRepo extends JpaRepository<Friends,Long> {

    boolean existsByUserAndFriend(UserEntity user, UserEntity friend);

    List<Friends> findByUser(UserEntity user);
    List<Friends> findByFriend(UserEntity friend);


    List<Friends> findByUserAndAccepted(UserEntity user, boolean accepted);

    Friends findByUserAndFriend(UserEntity user, UserEntity friend);

//    @Modifying
//    @Query("update Customer u set u.phone = :phone where u.id = :id")
//    void updatePhone(@Param(value = "id") long id, @Param(value = "phone") String phone);
    @Transactional
    @Modifying
    @Query(value = "UPDATE Friends SET accepted = :accepted WHERE id = :id")
    void updateStatus(@Param("id") Long id, @Param("accepted") boolean accepted);

    @Transactional
    void deleteById(Long id);
//    @Modifying
//    @Query("select * from Friends where status = :status")
//    List<Friends> select(@Param("status") int status);
}
