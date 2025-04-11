package com.squaregps.interview.model;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.Objects;

@Getter
@Setter
public class AVLData {

  private ZonedDateTime zonedDateTime;
  private Integer priority;
  private GPSElement gpsElement;
  private IOElement ioElement;

  public AVLData() {}

  public AVLData(
      ZonedDateTime zonedDateTime, Integer priority, GPSElement gpsElement, IOElement ioElement) {
    this.zonedDateTime = zonedDateTime;
    this.priority = priority;
    this.gpsElement = gpsElement;
    this.ioElement = ioElement;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    AVLData avlData = (AVLData) o;
    return Objects.equals(zonedDateTime, avlData.zonedDateTime)
        && Objects.equals(priority, avlData.priority)
        && Objects.equals(gpsElement, avlData.gpsElement)
        && Objects.equals(ioElement, avlData.ioElement);
  }

  @Override
  public int hashCode() {
    return Objects.hash(zonedDateTime, priority, gpsElement, ioElement);
  }

  @Override
  public String toString() {
    return "AVLData {"
        + "zonedDateTime="
        + zonedDateTime
        + ", priority="
        + priority
        + ", gpsElement="
        + gpsElement
        + ", ioElement="
        + ioElement
        + '}';
  }
}
