package com.stark.satos.storycreatorversion3.firestorefunctionality;

import java.util.ArrayList;

public class ArrayListForStoryContent {

    ArrayList<String> contentForStory = new ArrayList<>();

    public ArrayListForStoryContent() {
    }

    public ArrayListForStoryContent(ArrayList<String> contentForStory) {
        this.contentForStory = contentForStory;
    }

    public ArrayList<String> getContentForStory() {
        return contentForStory;
    }

    public void setContentForStory(ArrayList<String> contentForStory) {
        this.contentForStory = contentForStory;
    }
}