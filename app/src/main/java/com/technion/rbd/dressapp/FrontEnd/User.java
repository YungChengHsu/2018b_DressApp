package com.technion.rbd.dressapp.FrontEnd;

/**
 * Created by emad_ on 5/15/2018.
 */

public class User {
    private String userId;
    private String userEmail;
    private String userName;
    private String userGender;
    //private Measurement measurement;
    private String userProfilePic;
    private String userLocation;
    private String userToken;
    private double userRating;
    private Integer userNumberOrRatings;

    public User() {
    }

    public User(String userId, String userEmail, String userName, String userGender,/* Measurement measurement, */String userProfilePic) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userGender = userGender;
        // this.measurement = measurement;
        this.userProfilePic = userProfilePic;
        this.userRating = 0;
        this.userNumberOrRatings = 0;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public User(String userId, String userEmail, String userName, String userGender,
            /* Measurement measurement,*/ String userProfilePic, String userLocation,
                String userToken) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userGender = userGender;
        // this.measurement = measurement;
        this.userProfilePic = userProfilePic;
        this.userLocation = userLocation;
        this.userToken = userToken;
        this.userRating = 0;
        this.userNumberOrRatings = 0;
    }

    public User(String userId, String userEmail, String userName, String userGender,/*
                Measurement measurement,*/ String userProfilePic, String userLocation) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userName = userName;
        this.userGender = userGender;
        //   this.measurement = measurement;
        this.userProfilePic = userProfilePic;
        this.userLocation = userLocation;
        this.userRating = 0;
        this.userNumberOrRatings = 0;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getUserRating() {
        return userRating;
    }

    public void setUserRating(double userRating) {
        this.userRating = userRating;
    }

    public Integer getUserNumberOrRatings() {
        return userNumberOrRatings;
    }

    public void setUserNumberOrRatings(Integer userNumberOrRatings) {
        this.userNumberOrRatings = userNumberOrRatings;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

//    public void setMeasurement(Measurement measurement) {
//        this.measurement = measurement;
//    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userName='" + userName + '\'' +
                ", userGender='" + userGender + '\'' +
                '}';
    }

//    public Measurement getMeasurement() {
//        return measurement;
//        // TODO: How does this work
//    }
}
