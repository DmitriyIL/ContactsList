package com.lashchenov.contactsListApp.model;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.lashchenov.contactsListApp.UsersJsonParser;
import com.lashchenov.contactsListApp.pojo.User;

import java.util.Collections;
import java.util.List;

public class MainLiveData extends LiveData<List<User>> {
    private static MainLiveData sInstance;

    public static MainLiveData getInstance() {
        if (sInstance == null) {
            sInstance = new MainLiveData();
        }
        return sInstance;
    }

    private MainLiveData() {
        loadDataFromJson();
    }


    public void loadDataFromJson() {
        new JsonParserTask().execute();
    }


    private class JsonParserTask extends AsyncTask<Void, Void, List<User>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<User> doInBackground(Void... params) {
            UsersJsonParser usersJsonParser = new UsersJsonParser();
            return usersJsonParser.parseUsers();
        }


        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
            Collections.sort(users, (o1, o2) -> Integer.compare(o1.getId(), o2.getId()));
            setValue(users);
        }
    }
}
