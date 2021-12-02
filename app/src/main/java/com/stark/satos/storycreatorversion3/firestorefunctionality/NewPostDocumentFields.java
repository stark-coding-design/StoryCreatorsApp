package com.stark.satos.storycreatorversion3.firestorefunctionality;

public class NewPostDocumentFields {

    private String newPost;
    private String timeStamp;
    private String userName;
    private String title;
    private String storyId;
    private String userId;

    public NewPostDocumentFields() {
    }

    public NewPostDocumentFields(String newPost, String timeStamp, String title, String storyId, String userId, String userName) {
        this.newPost = newPost;
        this.timeStamp = timeStamp;
        this.title = title;
        this.userId =userId;
        this.userName = userName;
        this.storyId =storyId;
    }


    public NewPostDocumentFields(String newPost, String timeStamp, String title) {
        this.newPost = newPost;
        this.timeStamp = timeStamp;
        this.title =title;
    }

    public String getNewPost() {
        return newPost;
    }

    public void setNewPost(String newPost) {
        this.newPost = newPost;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}