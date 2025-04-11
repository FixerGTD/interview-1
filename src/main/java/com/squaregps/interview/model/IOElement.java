package com.squaregps.interview.model;

import com.google.common.base.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IOElement {

  private Boolean digitalInputStatus1;
  private Boolean digitalInputStatus2;
  private Double analogInput1;
  private Integer gsmLevel;

  public IOElement() {}

  public IOElement(
      Boolean digitalInputStatus1,
      Boolean digitalInputStatus2,
      Double analogInput1,
      Integer gsmLevel) {
    this.digitalInputStatus1 = digitalInputStatus1;
    this.digitalInputStatus2 = digitalInputStatus2;
    this.analogInput1 = analogInput1;
    this.gsmLevel = gsmLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    IOElement ioElement = (IOElement) o;
    return Objects.equal(digitalInputStatus1, ioElement.digitalInputStatus1)
        && Objects.equal(digitalInputStatus2, ioElement.digitalInputStatus2)
        && Objects.equal(analogInput1, ioElement.analogInput1)
        && Objects.equal(gsmLevel, ioElement.gsmLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(digitalInputStatus1, digitalInputStatus2, analogInput1, gsmLevel);
  }

  @Override
  public String toString() {
    return "IOElement {"
        + "digitalInputStatus1="
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
