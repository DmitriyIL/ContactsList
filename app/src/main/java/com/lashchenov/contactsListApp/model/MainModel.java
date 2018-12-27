package com.lashchenov.contactsListApp.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.lashchenov.contactsListApp.data.Data;
import com.lashchenov.contactsListApp.data.DataSQLiteImpl;
import com.lashchenov.contactsListApp.pojo.User;

import java.util.List;

public class MainModel extends AndroidViewModel {

    private final UsersLiveData usersLiveData;
    private final Data data;
    private boolean isWaiting;

    public MainModel(Application application) {
        super(application);
        usersLiveData = UsersLiveData.getInstance();
        data = new DataSQLiteImpl(getApplication());
    }

    public void loadUsers() {
        usersLiveData.loadDataFromJson();
        new DataSavingAsyncTask().execute();
    }

    public boolean isWaiting() {
        return isWaiting;
    }

    public void setWaiting(boolean waiting) {
        isWaiting = waiting;
    }

    public Data getData() {
        return data;
    }

    public LiveData<List<User>> getUsers() {
        return usersLiveData;
    }

    private class DataSavingAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... unused) {
            List<User> users = UsersLiveData.getInstance().getValue();
            if (users == null || users.isEmpty()) return null;
            data.setUsers(users);
            return null;
        }
    }
}
