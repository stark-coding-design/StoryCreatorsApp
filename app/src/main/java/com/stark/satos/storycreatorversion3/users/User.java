package com.stark.satos.storycreatorversion3.users;

public class User {

    private String profileUserName;
    private String userEmail;
    private String userBio;
    private String userPassword;
    private String profilePicUrl;

    public User() {
    }

    public User(String profileUserName){
        this.profileUserName = profileUserName;
    }

    public User(String profileUserName, String userEmail, String userPassword) {
        this.profileUserName = profileUserName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public User(String profileUserName, String userEmail, String userPassword, String profilePicUrl) {
        this.profileUserName = profileUserName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.profilePicUrl = profilePicUrl;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getProfileUserName() {
        return profileUserName;
    }

    public void setProfileUserName(String profileUserName) {
        this.profileUserName = profileUserName;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }
}