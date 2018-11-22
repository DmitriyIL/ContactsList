package com.lashchenov.contactsListApp.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.lashchenov.contactsListApp.pojo.User;

import java.util.List;

public class MainModel extends AndroidViewModel {

    private final MainLiveData dataLive;

    public MainModel(Application application) {
        super(application);
        dataLive = MainLiveData.getInstance();
    }

    public void loadUsers() {
        dataLive.loadDataFromJson();
    }

    public LiveData<List<User>> getDataLive() {
        return dataLive;
    }
}
