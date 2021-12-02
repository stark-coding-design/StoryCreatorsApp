package com.stark.satos.storycreatorversion3.firestorefunctionality;

public class DraftAttributes {

    String newPost;
    String timeStamp;
    String title;
    String storyId;

    public DraftAttributes() {
    }

    public DraftAttributes(String title, String newPost, String storyId, String timeStamp) {
        this.title = title;
        this.newPost = newPost;
        this.timeStamp = timeStamp;
        this.storyId =storyId;
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNewPost() {
        return newPost;
    }

    public void setNewPost(String newPost) {
        this.newPost = newPost;
    }
}