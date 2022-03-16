package ru.netcracker.bikepackerserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.validation.annotation.Validated;
import ru.netcracker.bikepackerserver.entity.PointEntity;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Validated
@JsonIgnoreProperties(ignoreUnknown = true)
public class PointModel {

    @NotNull
    private String description;

    @NotNull(message = "Latitude can not be null")
    private double latitude;

    @NotNull(message = "Longitude can not be null")
    private double longitude;

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

    public static PointEntity toEntity(PointModel model) {
        PointEntity entity = new PointEntity();

        entity.setLongitude(model.getLongitude());
        entity.setLatitude(model.getLatitude());
        entity.setDescription(model.getDescription());

        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PointModel)) return false;
        PointModel that = (PointModel) o;
        return Double.compare(that.getLatitude(), getLatitude()) == 0 && Double.compare(that.getLongitude(), getLongitude()) == 0 && Objects.equals(getDescription(), that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDescription(), getLatitude(), getLongitude());
    }

    @Override
    public String toString() {
        return "PointModel{" +
                "description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
