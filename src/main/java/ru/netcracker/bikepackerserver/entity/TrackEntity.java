package ru.netcracker.bikepackerserver.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="tracks", schema = "public")
public class TrackEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id")
    private Long track_id;

    @Column(name = "travel_time")
    private Long travel_time;

    @Column(name = "track_complexity")
    private short track_complexity;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private String gpx;

    public TrackEntity() {
    }

    public Long getTrack_id() {
        return track_id;
    }

    public void setTrack_id(Long track_id) {
        this.track_id = track_id;
    }

    public Long getTravel_time() {
        return travel_time;
    }

    public void setTravel_time(Long travel_time) {
        this.travel_time = travel_time;
    }

    public short getTrack_complexity() {
        return track_complexity;
    }

    public void setTrack_complexity(short track_complexity) {
        this.track_complexity = track_complexity;
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
                "track_id=" + track_id +
                ", travel_time=" + travel_time +
                ", track_complexity=" + track_complexity +
                ", user=" + user +
                ", gpx_url='" + gpx+ '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackEntity that = (TrackEntity) o;
        return Objects.equals(track_id, that.track_id) && Objects.equals(travel_time, that.travel_time) && Objects.equals(track_complexity, that.track_complexity) && Objects.equals(user, that.user) && Objects.equals(gpx, that.gpx);
    }

    @Override
    public int hashCode() {
        return Objects.hash(track_id, travel_time, track_complexity, user, gpx);
    }
}