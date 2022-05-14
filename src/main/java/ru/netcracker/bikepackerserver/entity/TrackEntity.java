package ru.netcracker.bikepackerserver.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.validation.annotation.Validated;
import ru.netcracker.bikepackerserver.exception.NoAnyFavoriteTrackException;
import ru.netcracker.bikepackerserver.exception.NoAnyUsersException;
import ru.netcracker.bikepackerserver.exception.NoSuchTrackException;
import ru.netcracker.bikepackerserver.model.TrackModel;
import ru.netcracker.bikepackerserver.repository.UserRepo;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name = "tracks", schema = "public")
@Validated
public class TrackEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "track_id")
    private Long trackId;

    @Column(name = "travel_time")
    private Long travelTime;

    @Column(name = "track_complexity")
    private double trackComplexity;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    @NotNull
    private UserEntity user;

    private String gpx;

    @Column(name = "track_name")
    private String trackName;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "track_date")
    private LocalDate trackDate;

    @Column(name = "track_distance")
    private Double trackDistance;

    @Column(name = "track_avg_speed")
    private Double trackAvgSpeed;

    @Column(name = "track_start_lat")
    private Double trackStartLat;

    @Column(name = "track_start_lon")
    private Double trackStartLon;

    @Column(name = "track_finish_lat")
    private Double trackFinishLat;

    @Column(name = "track_finish_lon")
    private Double trackFinishLon;

    public TrackEntity() {
    }

    public static TrackEntity toEntity(TrackModel model, UserRepo userRepo) throws NoAnyFavoriteTrackException {
        Optional<TrackModel> trackModel = Optional.ofNullable(model);
        TrackEntity trackEntity = new TrackEntity();
        UserEntity userEntity;

        if (trackModel.isPresent()) {
            Optional<Long> travelTime = Optional.ofNullable(trackModel.get().getTravelTime());
            Optional<@NotNull Double> trackComplexity = Optional.ofNullable(trackModel.get().getTrackComplexity());
            Optional<Long> userId = Optional.ofNullable(trackModel.get().getUser().getId());
            Optional<String> gpx = Optional.ofNullable(trackModel.get().getGpx());

            if (travelTime.isPresent()) {
                trackEntity.setTravelTime(trackModel.get().getTravelTime());
            } else {
                throw new NoSuchTrackException();
            }
            if (trackComplexity.isPresent()) {
                trackEntity.setTrackComplexity(trackModel.get().getTrackComplexity());
            } else {
                throw new NoSuchTrackException();
            }
            if (gpx.isPresent()) {
                trackEntity.setGpx(trackModel.get().getGpx());
            } else {
                throw new NoSuchTrackException();
            }
            if (userId.isPresent()) {
                userEntity = userRepo.findByid(userId.get());
                if (userEntity != null) {
                    trackEntity.setUser(userEntity);
                } else {
                    throw new NoAnyUsersException();
                }
            } else {
                throw new NoAnyUsersException();
            }
            return trackEntity;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public LocalDate getTrackDate() {
        return trackDate;
    }

    public void setTrackDate(LocalDate trackDate) {
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

    public void setTrackId(Long track_id) {
        this.trackId = track_id;
    }

    public Long getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Long travel_time) {
        this.travelTime = travel_time;
    }

    public @NotNull double getTrackComplexity() {
        return trackComplexity;
    }

    public void setTrackComplexity(@NotNull double track_complexity) {
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
                ", user=" + user +
                ", gpx_url='" + gpx + '\'' +
                '}';
    }
}