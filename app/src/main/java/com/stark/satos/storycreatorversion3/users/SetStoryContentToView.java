package com.stark.satos.storycreatorversion3.users;

public class SetStoryContentToView {

    private String storyContent;
    private String title;
    private String storyId;
    private String genre;
    private String ogAuthUsername;


    public SetStoryContentToView() {
    }

    public SetStoryContentToView(String genre, String ogAuthUsername, String storyContent, String storyId, String title) {
        this.genre = genre;
        this.ogAuthUsername = ogAuthUsername;
        this.storyContent = storyContent;
        this.title = title;
        this.storyId = storyId;
    }

    public String getOgAuthUsername() {
        return ogAuthUsername;
    }

    public void setOgAuthUsername(String ogAuthUsername) {
        this.ogAuthUsername = ogAuthUsername;
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

    public String getStoryContent() {
        return storyContent;
    }

    public void setStoryContent(String storyContent) {
        this.storyContent = storyContent;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
