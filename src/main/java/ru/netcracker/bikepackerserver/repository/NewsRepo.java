package ru.netcracker.bikepackerserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netcracker.bikepackerserver.entity.NewsEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;

import java.util.List;

@Repository
public interface NewsRepo extends JpaRepository<NewsEntity, Long> {
    List<NewsEntity> findByUser(UserEntity user);
}
