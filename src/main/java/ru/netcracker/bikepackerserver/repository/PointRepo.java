package ru.netcracker.bikepackerserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netcracker.bikepackerserver.entity.PointEntity;

import java.util.List;

@Repository
public interface PointRepo extends JpaRepository<PointEntity, Long> {

    PointEntity getById(Long id);

    List<PointEntity> findAll();
}