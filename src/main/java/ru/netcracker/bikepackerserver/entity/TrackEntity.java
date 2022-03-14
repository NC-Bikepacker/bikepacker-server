package ru.netcracker.bikepackerserver.entity;

import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="tracks", schema = "public")
public class TrackEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id")
    @NotNull
    private Long trackId;

    @Column(name = "travel_time")
    @NotNull
    private Long travelTime;

    @Column(name = "track_complexity")
    @NotNull
    private short trackComplexity;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private  UserEntity user;

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
                ", user="  +
                ", gpx_url='" + gpx+ '\'' +
                '}';
    }


}