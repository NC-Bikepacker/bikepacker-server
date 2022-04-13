package ru.netcracker.bikepackerserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.validation.annotation.Validated;
import ru.netcracker.bikepackerserver.entity.FavoriteTrackEntity;
import ru.netcracker.bikepackerserver.entity.ImageEntity;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.exception.BaseException;
import ru.netcracker.bikepackerserver.exception.ImageNotFoundException;
import ru.netcracker.bikepackerserver.exception.NoSuchTrackException;
import ru.netcracker.bikepackerserver.exception.UserNotFoundException;
import ru.netcracker.bikepackerserver.repository.ImageRepo;
import ru.netcracker.bikepackerserver.service.ImageService;
import ru.netcracker.bikepackerserver.service.TrackImageService;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Validated
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrackModel {

    @NotNull
    private Long trackId;

    @NotNull
    private Long travelTime;

    @NotNull
    private short trackComplexity;

    @NotNull
    private UserModel user;

    @NotNull
    private String gpx;

    @NotNull
    private String imageBase64;

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long trackId) {
        this.trackId = trackId;
    }

    public Long getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Long travelTime) {
        this.travelTime = travelTime;
    }

    public short getTrackComplexity() {
        return trackComplexity;
    }

    public void setTrackComplexity(short trackComplexity) {
        this.trackComplexity = trackComplexity;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getGpx() {
        return gpx;
    }

    public void setGpx(String gpx) {
        this.gpx = gpx;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackModel that = (TrackModel) o;
        return trackComplexity == that.trackComplexity && Objects.equals(trackId, that.trackId) && Objects.equals(travelTime, that.travelTime) && Objects.equals(user, that.user) && Objects.equals(gpx, that.gpx) && Objects.equals(imageBase64, that.imageBase64);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackId, travelTime, trackComplexity, user, gpx, imageBase64);
    }

    public static TrackModel toModel(TrackEntity trackEntity, ImageRepo imageRepo, TrackImageService trackImageService) throws BaseException {
        Optional<TrackEntity> entity = Optional.ofNullable(trackEntity);

        if (entity.isPresent()) {
            TrackModel model = new TrackModel();
            Optional<UserEntity> user = Optional.ofNullable(trackEntity.getUser());
            Optional<Long> trackId = Optional.ofNullable(trackEntity.getTrackId());
            Optional<Long> travelTime = Optional.ofNullable(trackEntity.getTravelTime());
            Optional<Short> trackComplexity = Optional.ofNullable(trackEntity.getTrackComplexity());
            Optional<String> gpx = Optional.ofNullable(trackEntity.getGpx());
            Optional<ImageEntity> image = Optional.ofNullable(imageRepo.findByTrack(trackEntity));

            if (trackId.isPresent()) {
                model.setTrackId(trackId.get());
            } else {
                throw new NoSuchTrackException();
            }

            if (travelTime.isPresent()) {
                model.setTravelTime(entity.get().getTravelTime());
            } else {
                throw new NoSuchTrackException();
            }

            if (trackComplexity.isPresent()) {
                model.setTrackComplexity(entity.get().getTrackComplexity());
            } else {
                throw new NoSuchTrackException();
            }

            if (gpx.isPresent()) {
                model.setGpx(entity.get().getGpx());
            } else {
                LoggerFactory.getLogger(TrackModel.class).error("gpx is null");
            }

            model.setUser(UserModel.toModel(user.orElseThrow(()->new UserNotFoundException())));


            if (image.isPresent()) {
                model.setImageBase64(image.get().getImageBase64());
            } else {
                if(gpx.isPresent()){
                    try {
                        trackImageService.saveImage(trackEntity);
                        model.setImageBase64(imageRepo.findByTrack(trackEntity).getImageBase64());
                    } catch (Exception e) {
                        LoggerFactory.getLogger(TrackModel.class).error("error save image, error message: " + e.getMessage());
                    }
                }
                else {
                    model.setImageBase64(null);
                    LoggerFactory.getLogger(TrackModel.class).error("image is null");
                }
            }

            return model;
            } else {
                throw new IllegalArgumentException();
        }
    }


    public static List<TrackModel> toModels(List<TrackEntity> trackEntities, ImageRepo imageRepo, TrackImageService trackImageService) throws BaseException {
        Optional<List<TrackEntity>> trackEntityList = Optional.ofNullable(trackEntities);
        List<TrackModel> trackModels = new ArrayList<>();

        if (trackEntityList.isPresent() && trackEntityList.get().size() > 0) {
            for (TrackEntity entity : trackEntityList.get()) {
                Optional<TrackEntity> trackEntity = Optional.ofNullable(entity);

                if (trackEntity.isPresent()) {
                    trackModels.add(TrackModel.toModel(trackEntity.get(), imageRepo, trackImageService));
                } else {
                    LoggerFactory.getLogger(TrackEntity.class).error("The trackEntity is null and is not added to the trackModels list.");
                }
            }
        } else {
            LoggerFactory.getLogger(TrackEntity.class).error("TrackEntity list in the arguments is null. The mapping operation cannot be performed.");
        }
        return trackModels;
    }

}
