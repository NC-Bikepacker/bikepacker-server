package ru.netcracker.bikepackerserver.entity;

import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import ru.netcracker.bikepackerserver.exception.NoSuchTrackException;
import ru.netcracker.bikepackerserver.exception.NullPointEntityException;
import ru.netcracker.bikepackerserver.exception.NullPointModelException;
import ru.netcracker.bikepackerserver.model.PointModel;
import ru.netcracker.bikepackerserver.repository.TrackRepo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "points", schema = "public")
@Validated
public class PointEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "point_id")
    private Long id;

    @Column(name = "point_describe")
    @NotNull
    private String description;

    @Column(name = "latitude")
    @NotNull(message = "Latitude can not be null")
    private double latitude;

    @Column(name = "longitude")
    @NotNull(message = "Longitude can not be null")
    private double longitude;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_track")
    @Nullable
    private TrackEntity track;

    public PointEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            this.description = "";
        } else {
            this.description = description;
        }
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
    public TrackEntity getTrack() {
        return track;
    }

    public void setTrack(@Nullable TrackEntity track) {
        this.track = track;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PointEntity)) return false;
        PointEntity entity = (PointEntity) o;
        return Double.compare(entity.getLatitude(), getLatitude()) == 0 && Double.compare(entity.getLongitude(), getLongitude()) == 0 && Objects.equals(getId(), entity.getId()) && Objects.equals(getDescription(), entity.getDescription()) && Objects.equals(getTrack(), entity.getTrack());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription(), getLatitude(), getLongitude(), getTrack());
    }

    @Override
    public String toString() {
        return "PointEntity{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", trackId=" + track.getTrackId() +
                '}';
    }

    public static PointEntity toEntity(PointModel model, TrackRepo trackRepo) throws NullPointModelException {
        Optional<PointModel> pointModel = Optional.ofNullable(model);

        if (pointModel.isPresent()) {
            PointEntity pointEntity = new PointEntity();
            Optional<TrackEntity> trackEntity = null;

            Optional<Long> modelTrackId = Optional.ofNullable(pointModel.get().getTrackId());
            double modelLongitude = pointModel.get().getLongitude();
            double modelLatitude = pointModel.get().getLatitude();
            String modelDescription = pointModel.get().getDescription();

            if (modelTrackId.isPresent()) {
                trackEntity = Optional.ofNullable(trackRepo.findTrackEntityByTrackId(modelTrackId.get()));

                if (trackEntity.isPresent()) {
                    pointEntity.setTrack(trackEntity.get());

                    pointEntity.setLongitude(modelLongitude);
                    pointEntity.setLatitude(modelLatitude);
                    pointEntity.setDescription(modelDescription);

                    return pointEntity;
                } else {
                    LoggerFactory.getLogger(PointEntity.class).info("Track with id " + modelTrackId + " not found. Point wasn't save");
                    throw new NoSuchTrackException();
                }
            }
            return null;
        } else {
            LoggerFactory.getLogger(PointEntity.class).error("PointModel in the arguments is null. The mapping operation cannot be performed.");
            throw new NullPointEntityException();
        }
    }

    public static List<PointEntity> toEntities(List<PointModel> pointModels, TrackRepo trackRepo) {
        Optional<List<PointModel>> models = Optional.ofNullable(pointModels);

        if (models.isPresent() && models.get().size() > 0) {
            List<PointEntity> pointEntities = new ArrayList<>(models.get().size());

            for (PointModel pointModel : pointModels) {
                Optional<PointModel> point = Optional.ofNullable(pointModel);

                if (point.isPresent()) {
                    pointEntities.add(Optional.ofNullable(PointEntity.toEntity(point.get(), trackRepo)).orElseThrow(NullPointEntityException::new));
                } else {
                    LoggerFactory.getLogger(PointEntity.class).error("The point is null and is not added to the PointEntity list.");
                }
            }

            return pointEntities;
        } else {
            LoggerFactory.getLogger(PointEntity.class).error("PointModel list in the arguments is null. The mapping operation cannot be performed.");
            throw new NullPointEntityException();
        }
    }
}
