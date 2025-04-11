package com.squaregps.interview.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class GPSElement {
  private Double longitude;
  private Double latitude;
  private Integer altitude;
  private Integer angle;
  private Integer satellites;
  private Integer speed;

  public GPSElement() {}

  public GPSElement(
      Double longitude,
      Double latitude,
      Integer altitude,
      Integer angle,
      Integer satellites,
      Integer speed) {
    this.longitude = longitude;
    this.latitude = latitude;
    this.altitude = altitude;
    this.angle = angle;
    this.satellites = satellites;
    this.speed = speed;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    GPSElement that = (GPSElement) o;
    return Objects.equals(longitude, that.longitude)
        && Objects.equals(latitude, that.latitude)
        && Objects.equals(altitude, that.altitude)
        && Objects.equals(angle, that.angle)
        && Objects.equals(satellites, that.satellites)
        && Objects.equals(speed, that.speed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(longitude, latitude, altitude, angle, satellites, speed);
  }

  @Override
  public String toString() {
    return "GPSElement {"
        + "longitude="
        + longitude
        + ", latitude="
        + latitude
        + ", altitude="
        + altitude
        + ", angle="
        + angle
        + ", satellites="
        + satellites
        + ", speed="
        + speed
        + '}';
  }
}
