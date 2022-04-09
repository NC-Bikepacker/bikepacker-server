package ru.netcracker.bikepackerserver.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.ImageEntity;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.exception.UserNotFoundException;
import ru.netcracker.bikepackerserver.model.TrackModel;
import ru.netcracker.bikepackerserver.repository.ImageRepo;
import ru.netcracker.bikepackerserver.repository.TrackRepo;
import ru.netcracker.bikepackerserver.repository.UserRepo;

import java.util.List;
import java.util.Optional;

@Service
public class TrackServiceImpl  implements  TrackService{

    @Autowired
    private TrackRepo trackRepo;

    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TrackImageService trackImageService;

    public TrackServiceImpl(TrackRepo trackRepo, ImageRepo imageRepo, UserRepo userRepo, TrackImageService trackImageService) {
        this.trackRepo = trackRepo;
        this.imageRepo = imageRepo;
        this.userRepo = userRepo;
        this.trackImageService = trackImageService;
    }

    @Override
    public void save(TrackModel track) {
        Optional<TrackModel> trackModel = Optional.ofNullable(track);
        if(trackModel.isPresent()){
            trackRepo.save(TrackEntity.toEntity(track, userRepo));
            try {
                trackImageService.saveImage(TrackEntity.toEntity(track, userRepo));
            } catch (Exception e) {
                LoggerFactory.getLogger(TrackServiceImpl.class).error("Error save track route image");
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void delete(Long trackId) {
        if(trackId != null){
            TrackEntity trackEntity = trackRepo.findTrackEntityByTrackId(trackId);
            ImageEntity imageEntity = imageRepo.findByTrack(trackEntity);
            trackRepo.deleteById(trackId);
            imageRepo.deleteById(imageEntity.getImageId());
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<TrackModel> getTracksForUser(Long userId) {
        if(userId!=null){
            Optional<UserEntity> userEntity = Optional.ofNullable(userRepo.findByid(userId));
            if(userEntity.isEmpty()){
                throw new UserNotFoundException(userId);
            }
            return TrackModel.toModels(trackRepo.findByUser(userEntity.get()), imageRepo);
        }
        else {
            throw new IllegalArgumentException();
        }
    }
    @Override
    public List<TrackModel> getAllTracks() {
        return TrackModel.toModels(trackRepo.findAll(), imageRepo);
    }
}
