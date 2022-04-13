package ru.netcracker.bikepackerserver.service;

import ru.netcracker.bikepackerserver.model.TrackModel;

import java.util.List;

public interface TrackService {
    void save(TrackModel track);
    void delete(Long trackId);
    void update(TrackModel trackModel);
    List<TrackModel> getAllTracks();
    List<TrackModel> getTracksForUser(Long userId);
}
