package ru.netcracker.bikepackerserver.service;

import ru.netcracker.bikepackerserver.model.TrackModel;

import java.util.List;

public interface TrackService {
    void save(TrackModel track);
    void delete(Long trackId);
    List<TrackModel> getAllTracks();
    List<TrackModel> getTracksForUser(Long userId);
}
