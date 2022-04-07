package ru.netcracker.bikepackerserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netcracker.bikepackerserver.entity.FavoriteTrackEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;

import java.util.List;

@Repository
public interface FavoriteTrackRepo extends JpaRepository<FavoriteTrackEntity, Long> {
    List<FavoriteTrackEntity> findByUser(UserEntity user);
}
