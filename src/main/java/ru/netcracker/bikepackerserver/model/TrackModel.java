package ru.netcracker.bikepackerserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import ru.netcracker.bikepackerserver.entity.ImageEntity;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.entity.UserEntity;
import ru.netcracker.bikepackerserver.exception.BaseException;
import ru.netcracker.bikepackerserver.exception.NoSuchTrackException;
import ru.netcracker.bikepackerserver.exception.UserNotFoundException;
import ru.netcracker.bikepackerserver.repository.ImageRepo;
import ru.netcracker.bikepackerserver.service.TrackImageService;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDate;
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

    private @NotNull double trackComplexity;

    @NotNull
    private UserModel user;

    @NotNull
    private String gpx;

    @NotNull
    private String imageBase64;
    @NotNull
    private String trackName;
    private Date trackDate;
    private Double trackDistance;
    private Double trackAvgSpeed;
    private Double trackStartLat;
    private Double trackStartLon;
    private Double trackFinishLat;
    private Double trackFinishLon;

    public static TrackModel toModel(TrackEntity trackEntity, ImageRepo imageRepo, TrackImageService trackImageService) throws BaseException {
        Optional<TrackEntity> entity = Optional.ofNullable(trackEntity);

        if (entity.isPresent()) {
            TrackModel model = new TrackModel();
            Optional<UserEntity> user = Optional.ofNullable(trackEntity.getUser());
            Optional<Long> trackId = Optional.ofNullable(trackEntity.getTrackId());
            Optional<Long> travelTime = Optional.ofNullable(trackEntity.getTravelTime());
            Optional<@NotNull Double> trackComplexity = Optional.of(trackEntity.getTrackComplexity());
            Optional<String> gpx = Optional.ofNullable(trackEntity.getGpx());
            Optional<ImageEntity> image = Optional.ofNullable(imageRepo.findByTrack(trackEntity));
            Optional<String> trackName = Optional.of(trackEntity.getTrackName());
            Optional<Date> trackDate = Optional.ofNullable(trackEntity.getTrackDate());
            Optional<Double> trackDistance = Optional.ofNullable(trackEntity.getTrackDistance());
            Optional<Double> trackAvgSpeed = Optional.ofNullable(trackEntity.getTrackAvgSpeed());
            Optional<Double> trackStartLat = Optional.ofNullable(trackEntity.getTrackStartLat());
            Optional<Double> trackStartLon = Optional.ofNullable(trackEntity.getTrackStartLon());
            Optional<Double> trackFinishLat = Optional.ofNullable(trackEntity.getTrackFinishLat());
            Optional<Double> trackFinishLon = Optional.ofNullable(trackEntity.getTrackFinishLon());

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

            model.setTrackComplexity(entity.get().getTrackComplexity());

            if (gpx.isPresent()) {
                model.setGpx(entity.get().getGpx());
            } else {
                LoggerFactory.getLogger(TrackModel.class).error("gpx is null");
            }

            model.setUser(UserModel.toModel(user.orElseThrow(UserNotFoundException::new)));
            model.setTrackName(entity.get().getTrackName());

            if (trackDate.isPresent()) {
                model.setTrackDate(entity.get().getTrackDate());
            }

            if (trackDistance.isPresent()) {
                model.setTrackDistance(entity.get().getTrackDistance());
            }

            if (trackAvgSpeed.isPresent()) {
                model.setTrackAvgSpeed(entity.get().getTrackAvgSpeed());
            }

            if (trackStartLat.isPresent()) {
                model.setTrackStartLat(entity.get().getTrackStartLat());
            }

            if (trackStartLon.isPresent()) {
                model.setTrackStartLon(entity.get().getTrackStartLon());
            }

            if (trackFinishLat.isPresent()) {
                model.setTrackFinishLat(entity.get().getTrackFinishLat());
            }

            if (trackFinishLon.isPresent()) {
                model.setTrackFinishLon(entity.get().getTrackFinishLon());
            }

            if (image.isPresent()) {
                model.setImageBase64(image.get().getImageBase64());
            } else {
                if (gpx.isPresent()) {
                    try {
                        trackImageService.saveImage(trackEntity);
                        model.setImageBase64(imageRepo.findByTrack(trackEntity).getImageBase64());
                    } catch (Exception e) {
                        LoggerFactory.getLogger(TrackModel.class).error("error save image, error message: " + e.getMessage());
                    }
                } else {
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

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public Date getTrackDate() {
        return trackDate;
    }

    public void setTrackDate(Date trackDate) {
        this.trackDate = trackDate;
    }

    public Double getTrackDistance() {
        return trackDistance;
    }

    public void setTrackDistance(Double trackDistance) {
        this.trackDistance = trackDistance;
    }

    public Double getTrackAvgSpeed() {
        return trackAvgSpeed;
    }

    public void setTrackAvgSpeed(Double trackAvgSpeed) {
        this.trackAvgSpeed = trackAvgSpeed;
    }

    public Double getTrackStartLat() {
        return trackStartLat;
    }

    public void setTrackStartLat(Double trackStartLat) {
        this.trackStartLat = trackStartLat;
    }

    public Double getTrackStartLon() {
        return trackStartLon;
    }

    public void setTrackStartLon(Double trackStartLon) {
        this.trackStartLon = trackStartLon;
    }

    public Double getTrackFinishLat() {
        return trackFinishLat;
    }

    public void setTrackFinishLat(Double trackFinishLat) {
        this.trackFinishLat = trackFinishLat;
    }

    public Double getTrackFinishLon() {
        return trackFinishLon;
    }

    public void setTrackFinishLon(Double trackFinishLon) {
        this.trackFinishLon = trackFinishLon;
    }

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

    public @NotNull double getTrackComplexity() {
        return trackComplexity;
    }

    public void setTrackComplexity(@NotNull double trackComplexity) {
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
        return trackComplexity == that.trackComplexity
                && Objects.equals(trackId, that.trackId)
                && Objects.equals(travelTime, that.travelTime)
                && Objects.equals(user, that.user)
                && Objects.equals(gpx, that.gpx)
                && Objects.equals(imageBase64, that.imageBase64)
                && Objects.equals(trackName, that.trackName)
                && Objects.equals(trackDate, that.trackDate)
                && Objects.equals(trackDistance, that.trackDistance)
                && Objects.equals(trackAvgSpeed, that.trackAvgSpeed)
                && Objects.equals(trackStartLat, that.trackStartLat)
                && Objects.equals(trackStartLon, that.trackStartLon)
                && Objects.equals(trackFinishLon, that.trackFinishLat)
                && Objects.equals(trackFinishLon, that.trackFinishLon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackId,
                travelTime,
                trackComplexity,
                user,
                gpx,
                imageBase64,
                trackName,
                trackDate,
                trackDistance,
                trackAvgSpeed,
                trackStartLat,
                trackStartLon,
                trackFinishLat,
                trackFinishLon
        );
    }

    @Override
    public String toString() {
        return "TrackModel{" +
                "trackId=" + trackId +
                ", travelTime=" + travelTime +
                ", trackComplexity=" + trackComplexity +
                ", user=" + user +
                ", gpx='" + gpx + '\'' +
                ", imageBase64='" + imageBase64 + '\'' +
                ", trackName='" + trackName + '\'' +
                ", trackDate='" + trackDate + '\'' +
                ", trackDistance='" + trackDistance + '\'' +
                ", trackAvgSpeed='" + trackAvgSpeed + '\'' +
                ", trackStartLat='" + trackStartLat + '\'' +
                ", trackStartLon='" + trackStartLon + '\'' +
                ", trackFinishLat='" + trackFinishLat + '\'' +
                ", trackFinishLon='" + trackFinishLon + '\'' +
                '}';
    }
}
