package com.stark.satos.storycreatorversion3.firestorefunctionality;

import java.util.ArrayList;
import java.util.List;

public class SetStoryId {
    List wholeStoryIdList = new ArrayList();

    public SetStoryId() {
    }

    public SetStoryId(List wholeStoryIdList) {
        this.wholeStoryIdList = wholeStoryIdList;
    }

    public List getWholeStoryIdList() {
        return wholeStoryIdList;
    }

    public void setWholeStoryIdList(List wholeStoryIdList) {
        this.wholeStoryIdList = wholeStoryIdList;
    }
}
