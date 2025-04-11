package com.squaregps.interview.model;

import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AVL {

  private Integer codecId;
  private Integer numberOfData;
  private List<AVLData> avlData;

  public AVL() {}

  public AVL(Integer codecId, Integer numberOfData) {
    this.codecId = codecId;
    this.numberOfData = numberOfData;
    this.avlData = new ArrayList<>(numberOfData);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    AVL avl = (AVL) o;
    return Objects.equals(codecId, avl.codecId)
        && Objects.equals(numberOfData, avl.numberOfData)
        && Objects.equals(avlData, avl.avlData);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codecId, numberOfData, avlData);
  }

  @Override
  public String toString() {
    return "AVL {"
        + "codecId="
        + codecId
        + ", numberOfData="
        + numberOfData
        + ", avlData="
        + avlData
        + '}';
  }
}
