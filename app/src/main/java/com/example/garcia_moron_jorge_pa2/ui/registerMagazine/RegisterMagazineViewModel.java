package com.example.garcia_moron_jorge_pa2.ui.registerMagazine;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterMagazineViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RegisterMagazineViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}