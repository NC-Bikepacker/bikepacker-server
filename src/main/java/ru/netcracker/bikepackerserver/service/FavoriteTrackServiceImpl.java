package ru.netcracker.bikepackerserver.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.FavoriteTrackEntity;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.exception.BaseException;
import ru.netcracker.bikepackerserver.exception.UserNotFoundException;
import ru.netcracker.bikepackerserver.model.TrackModel;
import ru.netcracker.bikepackerserver.repository.FavoriteTrackRepo;
import ru.netcracker.bikepackerserver.repository.ImageRepo;
import ru.netcracker.bikepackerserver.repository.TrackRepo;
import ru.netcracker.bikepackerserver.repository.UserRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteTrackServiceImpl implements FavoriteTrackService{

    @Autowired
    private final FavoriteTrackRepo favoriteTrackRepo;

    @Autowired
    private final ImageRepo imageRepo;

    @Autowired
    private final UserRepo userRepo;

    @Autowired
    private final TrackRepo trackRepo;

    @Autowired
    private final TrackImageService trackImageService;


    public FavoriteTrackServiceImpl(FavoriteTrackRepo favoriteTrackRepo, ImageRepo imageRepo, UserRepo userRepo, TrackRepo trackRepo, TrackImageService trackImageService) {
        this.favoriteTrackRepo = favoriteTrackRepo;
        this.imageRepo = imageRepo;
        this.userRepo = userRepo;
        this.trackRepo = trackRepo;
        this.trackImageService = trackImageService;
    }

    @Override
    public void save(FavoriteTrackEntity favoriteTrackEntity) {
        try {
            favoriteTrackEntity = favoriteTrackRepo.save(favoriteTrackEntity);
        } catch (Exception e) {
            LoggerFactory.getLogger(FavoriteTrackEntity.class).error("Saving FavoriteTrack error." + e);
        }
    }

    @Override
    public List<TrackModel> getTracksByUserId(Long userId) throws BaseException {
        if (userId != null) {
            Optional<UserEntity> user = Optional.ofNullable(userRepo.findByid(userId));
            if (user.isPresent()) {
                List<FavoriteTrackEntity> favoriteTrackEntities = favoriteTrackRepo.findByUser(user.get());
                List<TrackEntity> trackEntities = new ArrayList<>();
                List<TrackModel> trackModels = new ArrayList<>();
                if (favoriteTrackEntities.size() > 0) {
                    trackModels.addAll(TrackModel.toModels(FavoriteTrackEntity.toTrackEntities(favoriteTrackEntities), imageRepo, trackImageService));
                } else {
                    LoggerFactory.getLogger(FavoriteTrackService.class).error("There are no tracks");
                }
                return trackModels;
            } else {
                throw new UserNotFoundException(userId);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<TrackModel> getTracks() throws BaseException {
        List<TrackEntity> trackEntities = trackRepo.findAll();
        return TrackModel.toModels(trackEntities, imageRepo, trackImageService);
    }
}
