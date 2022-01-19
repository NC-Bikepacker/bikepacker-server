package ru.netcracker.bikepackerserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netcracker.bikepackerserver.entity.TrackEntity;

public interface TrackRepo extends JpaRepository<TrackEntity, Long> {
}
