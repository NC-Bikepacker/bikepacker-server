package ru.netcracker.bikepackerserver.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.ImageEntity;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.exception.TrackNotFoundException;
import ru.netcracker.bikepackerserver.exception.UserNotFoundException;
import ru.netcracker.bikepackerserver.model.TrackModel;
import ru.netcracker.bikepackerserver.repository.ImageRepo;
import ru.netcracker.bikepackerserver.repository.TrackRepo;
import ru.netcracker.bikepackerserver.repository.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrackServiceImpl implements TrackService {

    @Autowired
    private TrackRepo trackRepo;

    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TrackImageService trackImageService;

    @Autowired
    private FriendService friendService;

    public TrackServiceImpl(TrackRepo trackRepo, ImageRepo imageRepo, UserRepo userRepo, TrackImageService trackImageService, FriendService friendService) {
        this.trackRepo = trackRepo;
        this.imageRepo = imageRepo;
        this.userRepo = userRepo;
        this.trackImageService = trackImageService;
        this.friendService = friendService;
    }

    @Override
    public TrackEntity save(TrackModel track) {
        Optional<TrackModel> trackModel = Optional.ofNullable(track);
        if (trackModel.isPresent()) {
            TrackEntity trackEntity = new TrackEntity();
            trackEntity.setTrackId(track.getTrackId());
            trackEntity.setTrackComplexity(track.getTrackComplexity());
            trackEntity.setTravelTime(track.getTravelTime());
            trackEntity.setGpx(track.getGpx());
            trackEntity.setUser(userRepo.findByid(track.getUser().getId()));
            trackEntity.setTrackName(track.getTrackName());
            trackEntity.setTrackDate(track.getTrackDate());
            trackEntity.setTrackDistance(track.getTrackDistance());
            trackEntity.setTrackAvgSpeed(track.getTrackAvgSpeed());
            trackEntity.setTrackStartLat(track.getTrackStartLat());
            trackEntity.setTrackStartLon(track.getTrackStartLon());
            trackEntity.setTrackFinishLat(track.getTrackFinishLat());
            trackEntity.setTrackFinishLon(track.getTrackFinishLon());
            return trackRepo.save(trackEntity);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void delete(Long trackId) {
        if (trackId != null) {
            TrackEntity trackEntity = trackRepo.findTrackEntityByTrackId(trackId);
            ImageEntity imageEntity = imageRepo.findByTrack(trackEntity);
            trackRepo.deleteById(trackId);
            imageRepo.deleteById(imageEntity.getImageId());
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void update(TrackModel trackModel) {
        TrackEntity trackEntity = trackRepo.getById(trackModel.getTrackId());
        if (trackEntity != null) {
            trackEntity.setUser(userRepo.findByid(trackModel.getUser().getId()));
            trackEntity.setTrackComplexity(trackModel.getTrackComplexity());
            trackEntity.setTravelTime(trackModel.getTravelTime());
            trackEntity.setGpx(trackModel.getGpx());
            trackEntity.setTrackName(trackModel.getTrackName());
            trackEntity.setTrackDate(trackModel.getTrackDate());
            trackEntity.setTrackDistance(trackModel.getTrackDistance());
            trackEntity.setTrackAvgSpeed(trackModel.getTrackAvgSpeed());
            trackEntity.setTrackStartLat(trackModel.getTrackStartLat());
            trackEntity.setTrackStartLon(trackModel.getTrackStartLon());
            trackEntity.setTrackFinishLat(trackModel.getTrackFinishLat());
            trackEntity.setTrackFinishLon(trackModel.getTrackFinishLon());
            trackRepo.save(trackEntity);
            if (!trackModel.getGpx().isBlank()) {
                saveImageForTrack(trackEntity);
            }

        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<TrackModel> getTracksForUser(Long userId) {
        if (userId != null) {
            Optional<UserEntity> userEntity = Optional.ofNullable(userRepo.findByid(userId));
            if (userEntity.isEmpty()) {
                throw new UserNotFoundException(userId);
            }
            return TrackModel.toModels(trackRepo.findByUser(userEntity.get()), imageRepo, trackImageService);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<TrackModel> getAllTracks() {
        return TrackModel.toModels(trackRepo.findAll(), imageRepo, trackImageService);
    }

    private void saveImageForTrack(TrackEntity trackEntity) {
        ImageEntity imageEntity = imageRepo.findByTrack(trackEntity);

        if (imageEntity != null) {
            imageRepo.deleteById(imageEntity.getImageId());
        }
        try {
            trackImageService.saveImage(trackEntity);
        } catch (Exception e) {
            LoggerFactory.getLogger(TrackServiceImpl.class).error("Error save image for trackEntity. Track entity id is: " + trackEntity.getTrackId() + ". Exception message: " + e.getMessage(), e);
        }
    }

    @Override
    public TrackModel getOneTrack(Long trackId) {
        TrackEntity trackEntity = trackRepo.findTrackEntityByTrackId(trackId);
        if (trackEntity != null) {
            return TrackModel.toModel(trackEntity, imageRepo, trackImageService);
        } else {
            throw new TrackNotFoundException(trackId);
        }

    }

    @Override
    public List<TrackModel> getLastFriendTracks(Long userId) {
        List<TrackEntity> trackEntities = new ArrayList<>();
        List<UserEntity> friends = friendService.getFriends(userId);

        friends.stream()
                .map(p -> trackRepo.findByUser(p))
                .filter(Objects::nonNull)
                .forEach(p -> trackEntities.addAll(p));

        return trackEntities.stream()
                .map(p -> TrackModel.toModel(p, imageRepo, trackImageService))
                .collect(Collectors.toList());
    }
}
