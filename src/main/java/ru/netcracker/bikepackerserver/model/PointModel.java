package ru.netcracker.bikepackerserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import ru.netcracker.bikepackerserver.entity.PointEntity;
import ru.netcracker.bikepackerserver.entity.TrackEntity;
import ru.netcracker.bikepackerserver.exception.NullPointModelException;
import ru.netcracker.bikepackerserver.repository.TrackRepo;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Validated
@JsonIgnoreProperties(ignoreUnknown = true)
public class PointModel {

    @NotNull
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

    public static Optional<PointEntity> toEntity(PointModel model, TrackRepo trackRepo) throws NullPointModelException {
        PointEntity pointEntity = null;

        if (model != null) {
            pointEntity = new PointEntity();
            TrackEntity trackEntity = null;

            Long modelTrackId = model.getTrackId();
            double modelLongitude = model.getLongitude();
            double modelLatitude = model.getLatitude();
            String modelDescription = model.getDescription();

            if (modelTrackId != null) {
                trackEntity = trackRepo.findByTrackId(modelTrackId);
                if (trackEntity == null) {
                    LoggerFactory.getLogger(PointEntity.class).info("Track with id " + modelTrackId + " not found.");
                }
            }

            pointEntity.setTrack(trackEntity);
            pointEntity.setLongitude(modelLongitude);
            pointEntity.setLatitude(modelLatitude);
            pointEntity.setDescription(modelDescription);
        } else {
            throw new NullPointModelException();
        }

        return Optional.ofNullable(pointEntity);
    }
}
