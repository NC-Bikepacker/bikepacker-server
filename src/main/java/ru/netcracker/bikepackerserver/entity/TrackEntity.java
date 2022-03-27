package ru.netcracker.bikepackerserver.entity;

import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tracks", schema = "public")
@Validated
public class TrackEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id")
    private Long trackId;

    @Column(name = "travel_time")
    @NotNull
    private Long travelTime;

    @Column(name = "track_complexity")
    @NotNull
    private short trackComplexity;

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private UserEntity user;

    private String gpx;

    public TrackEntity() {
    }

    public Long getTrackId() {
        return trackId;
    }

    public void setTrackId(Long track_id) {
        this.trackId = track_id;
    }

    public Long getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Long travel_time) {
        this.travelTime = travel_time;
    }

    public short getTrackComplexity() {
        return trackComplexity;
    }

    public void setTrackComplexity(short track_complexity) {
        this.trackComplexity = track_complexity;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getGpx() {
        return gpx;
    }

    public void setGpx(String gpx) {
        this.gpx = gpx;
    }

    @Override
    public String toString() {
        return "TrackEntity{" +
                "track_id=" + trackId +
                ", travel_time=" + travelTime +
                ", track_complexity=" + trackComplexity +
                ", userId=" + user.getId() +
                ", gpx_url='" + gpx + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackEntity that = (TrackEntity) o;
        return Objects.equals(trackId, that.trackId) && Objects.equals(travelTime, that.travelTime) && Objects.equals(trackComplexity, that.trackComplexity) && Objects.equals(user, that.user) && Objects.equals(gpx, that.gpx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackId, travelTime, trackComplexity, user, gpx);
    }
}