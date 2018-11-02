package com.lashchenov.contactsListApp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.lashchenov.contactsListApp.R;
import com.lashchenov.contactsListApp.adapter.UsersAdapter;
import com.lashchenov.contactsListApp.jsonParse.JsonParserTask;
import com.lashchenov.contactsListApp.pojo.User;

import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    private RecyclerView usersView;
    private UsersAdapter usersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();

        loadUsers();
    }


    public void initRecyclerView() {
        usersView = findViewById(R.id.recyclerView);
        usersView.setLayoutManager(new LinearLayoutManager(this));

        UsersAdapter.OnUserClickListener onUserClickListener = new UsersAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(User user) {
                if (user.getState()) {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.putExtra(ProfileActivity.USER_ID, user);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, user.getName() + " is inactive",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        usersAdapter = new UsersAdapter(onUserClickListener, getBaseContext());
        usersView.setAdapter(usersAdapter);
    }


    private void loadUsers() {
        JsonParserTask jsonParserTask = new JsonParserTask(this);
        jsonParserTask.execute();
        try {
            usersAdapter.setItems(jsonParserTask.get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

