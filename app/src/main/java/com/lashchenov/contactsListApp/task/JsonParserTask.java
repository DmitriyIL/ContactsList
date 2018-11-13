package com.lashchenov.contactsListApp.task;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.lashchenov.contactsListApp.R;
import com.lashchenov.contactsListApp.UsersJsonParser;
import com.lashchenov.contactsListApp.activity.MainActivity;
import com.lashchenov.contactsListApp.adapter.UsersAdapter;
import com.lashchenov.contactsListApp.data.Data;
import com.lashchenov.contactsListApp.data.DataSQLiteImpl;
import com.lashchenov.contactsListApp.pojo.User;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JsonParserTask extends AsyncTask<Void, Void, List<User>> {

    private WeakReference<Context> context;
    private Data data;
    private UsersAdapter usersAdapter;
    private WeakReference<View> progressBarView;

    //only Main Activity can call that task
    public JsonParserTask(MainActivity activity) {
        this.context = new WeakReference<>(activity.getBaseContext());
        data = new DataSQLiteImpl(context.get());
        usersAdapter = activity.getUsersAdapter();
        progressBarView = new WeakReference<>(activity.findViewById(R.id.progressBar));
    }
    

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBarView.get().setVisibility(View.VISIBLE);
        usersAdapter.clearItems();
    }

    @Override
    protected List<User> doInBackground(Void... params) {
        UsersJsonParser usersJsonParser = new UsersJsonParser();
        return usersJsonParser.parseUsers();
    }


    @Override
    protected void onPostExecute(List<User> users) {
        super.onPostExecute(users);
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        });
        progressBarView.get().setVisibility(View.INVISIBLE);
        Toast.makeText(context.get(), "Users loaded", Toast.LENGTH_SHORT).show();
        usersAdapter.setItems(users);
        data.setUsers(users);
    }

}

