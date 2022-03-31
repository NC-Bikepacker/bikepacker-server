package ru.netcracker.bikepackerserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import ru.netcracker.bikepackerserver.entity.PointEntity;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.exception.BaseException;
import ru.netcracker.bikepackerserver.service.ImageService;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Validated
@JsonIgnoreProperties(ignoreUnknown = true)
public class PointModel {

    @NotNull(message = "Description can not be null")
    private String description;

    @NotNull(message = "Latitude can not be null")
    private double latitude;

    @NotNull(message = "Longitude can not be null")
    private double longitude;

    @Nullable
    private Long trackId;

    @Nullable
    private List<String> images;

    public PointModel() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Nullable
    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(@Nullable Long trackId) {
        this.trackId = trackId;
    }

    @Nullable
    public List<String> getImages() {
        return images;
    }

    public void setImages(@Nullable List<String> images) {
        this.images = images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PointModel)) return false;
        PointModel that = (PointModel) o;
        return Double.compare(that.getLatitude(), getLatitude()) == 0 && Double.compare(that.getLongitude(), getLongitude()) == 0 && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getTrackId(), that.getTrackId()) && Objects.equals(getImages(), that.getImages());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), getLatitude(), getLongitude(), getTrackId(), getImages());
    }

    public static PointModel toModel(PointEntity pointEntity, ImageService imageService) throws BaseException {
        Optional<PointEntity> entity = Optional.ofNullable(pointEntity);

        if (entity.isPresent()) {
            PointModel model = new PointModel();
            Long pointId = entity.get().getId();

            Optional<TrackEntity> track = Optional.ofNullable(entity.get().getTrack());

            if (track.isPresent()) {
                Optional<Long> trackId = Optional.ofNullable(track.get().getTrackId());

                if (trackId.isPresent()) {
                    model.setTrackId(trackId.get());
                }
            } else {
                model.setTrackId(null);
            }

            List<String> images = imageService.getImagesBase64ByPointId(pointId);
            model.setImages(images);
            model.setDescription(entity.get().getDescription());
            model.setLatitude(entity.get().getLatitude());
            model.setLongitude(entity.get().getLongitude());

            return model;
        } else {
            LoggerFactory.getLogger(PointEntity.class).error("PointEntity in the arguments is null. The mapping operation cannot be performed.");
            return null;
        }
    }

    public static List<PointModel> toModels(List<PointEntity> pointEntities, ImageService imageService) throws BaseException {
        Optional<List<PointEntity>> pointEntityList = Optional.ofNullable(pointEntities);
        List<PointModel> pointModels = new ArrayList<>();

        if (pointEntityList.isPresent() && pointEntityList.get().size() > 0) {
            for (PointEntity entity : pointEntityList.get()) {
                Optional<PointEntity> pointEntity = Optional.ofNullable(entity);

                if (pointEntity.isPresent()) {
                    pointModels.add(PointModel.toModel(pointEntity.get(), imageService));
                } else {
                    LoggerFactory.getLogger(PointEntity.class).error("The point is null and is not added to the PointModels list.");
                }
            }
        } else {
            LoggerFactory.getLogger(PointEntity.class).error("PointEntity list in the arguments is null. The mapping operation cannot be performed.");
        }

        return pointModels;
    }
}
