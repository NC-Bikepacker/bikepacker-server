package ru.netcracker.bikepackerserver.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.netcracker.bikepackerserver.entity.ImageEntity;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.exception.UserNotFoundException;
import ru.netcracker.bikepackerserver.model.TrackModel;
import ru.netcracker.bikepackerserver.model.UserModel;
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
    public TrackEntity save(TrackModel track) {
        Optional<TrackModel> trackModel = Optional.ofNullable(track);
        if(trackModel.isPresent()){
            TrackEntity trackEntity = new TrackEntity();
            trackEntity.setTrackId(track.getTrackId());
            trackEntity.setTrackComplexity(track.getTrackComplexity());
            trackEntity.setTravelTime(track.getTravelTime());
            trackEntity.setGpx(track.getGpx());
            trackEntity.setUser(userRepo.findByid(track.getUser().getId()));
            System.out.println( "trackEntity to sting and save" + trackEntity.toString() );
            return trackRepo.save(trackEntity);
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
    public void update(TrackModel trackModel) {
        System.out.println("model trackid: " + trackModel.getTrackId());
        TrackEntity trackEntity = trackRepo.getById(trackModel.getTrackId());
        System.out.println("trackEntity id ="+ trackEntity.getTrackId() + " track travel time" + trackEntity.getTravelTime());
            if(trackEntity != null) {
                trackEntity.setUser(userRepo.findByid(trackModel.getUser().getId()));
                trackEntity.setTrackComplexity(trackModel.getTrackComplexity());
                trackEntity.setTravelTime(trackModel.getTravelTime());
                trackEntity.setGpx(trackModel.getGpx());
                trackRepo.save(trackEntity);
                System.out.println("track save");
                if(!trackModel.getGpx().isBlank()){
                    saveImageForTrack(trackEntity);
                    System.out.println("image save");
                }

            }
            else {
                System.out.println("track not put");
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
            return TrackModel.toModels(trackRepo.findByUser(userEntity.get()), imageRepo, trackImageService);
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<TrackModel> getAllTracks() {
        return TrackModel.toModels(trackRepo.findAll(), imageRepo, trackImageService);
    }

    private void saveImageForTrack(TrackEntity trackEntity){
        ImageEntity imageEntity = imageRepo.findByTrack(trackEntity);

        if(imageEntity != null){
            imageRepo.deleteById(imageEntity.getImageId());
        }

            try {
                trackImageService.saveImage(trackEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
