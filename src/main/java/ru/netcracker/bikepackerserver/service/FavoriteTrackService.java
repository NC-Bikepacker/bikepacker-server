package ru.netcracker.bikepackerserver.service;

import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.FavoriteTrackEntity;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.model.TrackModel;

import java.util.List;

public interface FavoriteTrackService {
    void save(FavoriteTrackEntity favoriteTrackEntity);
    List<TrackModel> getTracksByUserId(Long userId);
    List<TrackModel> getTracks();
}
