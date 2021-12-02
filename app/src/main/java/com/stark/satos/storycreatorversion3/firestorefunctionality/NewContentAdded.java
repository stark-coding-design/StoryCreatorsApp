package com.stark.satos.storycreatorversion3.firestorefunctionality;

public class NewContentAdded {
    String authUserName;
    String content;
    String timeStamp;
    String uid;

    public NewContentAdded() {
    }

    public NewContentAdded(String authUserName, String content, String timeStamp, String uid) {
        this.authUserName = authUserName;
        this.content = content;
        this.timeStamp = timeStamp;
        this.uid = uid;
    }

    public String getAuthUserName() {
        return authUserName;
    }

    public void setAuthUserName(String authUserName) {
        this.authUserName = authUserName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

