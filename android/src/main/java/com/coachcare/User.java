package com.coachcare.qnsdk;

import java.util.Date;

public class User {
  private String userId;
  private int height;
  private String gender;
  private Date birthDay;
  private int athleteType;
  private int choseShape;
  private int choseGoal;
  private double clothesWeight;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public Date getBirthDay() {
    return birthDay;
  }

  public void setBirthDay(Date birthDay) {
    this.birthDay = birthDay;
  }

  public User() {
  }

  public int getAthleteType() {
    return athleteType;
  }

  public void setAthleteType(int athleteType) {
    this.athleteType = athleteType;
  }

  public int getChoseShape() {
    return choseShape;
  }

  public void setChoseShape(int choseShape) {
    this.choseShape = choseShape;
  }

  public int getChoseGoal() {
    return choseGoal;
  }

  public void setChoseGoal(int choseGoal) {
    this.choseGoal = choseGoal;
  }

  public double getClothesWeight() {
    return clothesWeight;
  }

  public void setClothesWeight(double clothesWeight) {
    this.clothesWeight = clothesWeight;
  }
}
