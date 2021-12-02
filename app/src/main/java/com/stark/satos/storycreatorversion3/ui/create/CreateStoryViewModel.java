package com.stark.satos.storycreatorversion3.ui.create;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateStoryViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CreateStoryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}