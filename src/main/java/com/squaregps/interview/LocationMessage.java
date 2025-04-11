package com.squaregps.interview;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
public class LocationMessage {

  private ZonedDateTime dateTime;
  private Double longitude;
  private Double latitude;
  private Integer altitude;
  private Integer angle;
  private Integer satellites;
  private Integer speed;
  private Boolean digitalInputStatus1;
  private Boolean digitalInputStatus2;
  private Double analogInput1;
  private Integer gsmLevel;

  public LocationMessage() {}

  public LocationMessage(
      ZonedDateTime dateTime,
      Double longitude,
      Double latitude,
      Integer altitude,
      Integer angle,
      Integer satellites,
      Integer speed,
      Boolean digitalInputStatus1,
      Boolean digitalInputStatus2,
      Double analogInput1,
      Integer gsmLevel) {
    this.dateTime = dateTime;
    this.longitude = longitude;
    this.latitude = latitude;
    this.altitude = altitude;
    this.angle = angle;
    this.satellites = satellites;
    this.speed = speed;
    this.digitalInputStatus1 = digitalInputStatus1;
    this.digitalInputStatus2 = digitalInputStatus2;
    this.analogInput1 = analogInput1;
    this.gsmLevel = gsmLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LocationMessage that = (LocationMessage) o;
    return Objects.equals(dateTime, that.dateTime)
        && Objects.equals(longitude, that.longitude)
        && Objects.equals(latitude, that.latitude)
        && Objects.equals(altitude, that.altitude)
        && Objects.equals(angle, that.angle)
        && Objects.equals(satellites, that.satellites)
        && Objects.equals(speed, that.speed)
        && Objects.equals(digitalInputStatus1, that.digitalInputStatus1)
        && Objects.equals(digitalInputStatus2, that.digitalInputStatus2)
        && Objects.equals(analogInput1, that.analogInput1)
        && Objects.equals(gsmLevel, that.gsmLevel);
  }

  @Override
  public int hashCode() {
    //noinspection ObjectInstantiationInEqualsHashCode
    return Objects.hash(
        dateTime,
        longitude,
        latitude,
        altitude,
        angle,
        satellites,
        speed,
        digitalInputStatus1,
        digitalInputStatus2,
        analogInput1,
        gsmLevel);
  }

  @Override
  public String toString() {
    return "LocationMessage{"
        + "dateTime="
        + dateTime
        + ", longitude="
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
        + ", digitalInputStatus1="
        + digitalInputStatus1
        + ", digitalInputStatus2="
        + digitalInputStatus2
        + ", analogInput1="
        + analogInput1
        + ", gsmLevel="
        + gsmLevel
        + '}';
  }
}
