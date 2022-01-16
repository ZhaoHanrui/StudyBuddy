package com.example.studybuddy.ui.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MapViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("map view");
    }

    public LiveData<String> getText() {
        return mText;
    }
}