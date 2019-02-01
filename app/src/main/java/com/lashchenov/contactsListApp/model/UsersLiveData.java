package com.lashchenov.contactsListApp.model;

import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.lashchenov.contactsListApp.UsersJsonParser;
import com.lashchenov.contactsListApp.pojo.User;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UsersLiveData extends MutableLiveData<List<User>> {
    private static UsersLiveData sInstance;

    public static UsersLiveData getInstance() {
        if (sInstance == null) {
            sInstance = new UsersLiveData();
        }
        return sInstance;
    }

    private UsersLiveData() {
        /*Callable<List<User>> task = () -> new UsersJsonParser().parseUsers();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<List<User>> future = executor.submit(task);

        Observable<List<User>> ob = Observable.fromFuture(future)
                .subscribeOn(AndroidSchedulers.mainThread());*/

        loadUsersFromJson();
    }


    public void loadUsersFromJson() {
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
            if (users != null) {
                Collections.sort(users, (o1, o2) -> Integer.compare(o1.getId(), o2.getId()));
            }
            setValue(users);
        }
    }
}
