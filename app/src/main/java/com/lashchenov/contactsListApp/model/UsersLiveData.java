package com.lashchenov.contactsListApp.model;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.lashchenov.contactsListApp.UsersJsonParser;
import com.lashchenov.contactsListApp.pojo.User;

import java.util.Collections;
import java.util.List;

public class UsersLiveData extends MutableLiveData<List<User>> {
    private static UsersLiveData sInstance;

    public static UsersLiveData getInstance() {
        if (sInstance == null) {
            sInstance = new UsersLiveData();
        }
        return sInstance;
    }

    private UsersLiveData() {
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
