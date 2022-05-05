package ru.netcracker.bikepackerserver.service;

import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.model.TrackModel;

import java.util.List;

public interface TrackService {
    TrackEntity save(TrackModel track);
    void delete(Long trackId);
    void update(TrackModel trackModel);
    TrackModel getOneTrack(Long id);
    List<TrackModel> getAllTracks();
    List<TrackModel> getTracksForUser(Long userId);
    List<TrackModel> getLastFriendTracks(Long userId);
}
