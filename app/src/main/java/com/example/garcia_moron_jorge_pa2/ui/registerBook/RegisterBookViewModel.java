package com.example.garcia_moron_jorge_pa2.ui.registerBook;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterBookViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RegisterBookViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}