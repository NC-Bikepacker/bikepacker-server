package ru.netcracker.bikepackerserver.entity;

import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import ru.netcracker.bikepackerserver.model.PointModel;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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

    public static PointModel toModel(PointEntity entity) {
        PointModel model = new PointModel();

        model.setDescription(entity.getDescription());
        model.setLatitude(entity.getLatitude());
        model.setLongitude(entity.getLongitude());

        return model;
    }
}
