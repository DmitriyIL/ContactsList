package com.lashchenov.contactsListApp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lashchenov.contactsListApp.R;
import com.lashchenov.contactsListApp.adapter.UsersAdapter;
import com.lashchenov.contactsListApp.data.Data;
import com.lashchenov.contactsListApp.data.DataSQLiteImpl;
import com.lashchenov.contactsListApp.task.JsonParserTask;
import com.lashchenov.contactsListApp.pojo.User;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RecyclerView usersView;
    private UsersAdapter usersAdapter;
    private Toolbar toolbar;

    private Data data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = new DataSQLiteImpl(this);

        initToolbar();
        initRecyclerView();
        fillRecyclerView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.round_cache) {
            loadUsersFromJson();
            return true;
        }
        return false;
    }


    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    private void initRecyclerView() {
        final Context context = getBaseContext();

        usersView = findViewById(R.id.usersRecyclerView);
        usersView.setLayoutManager(new LinearLayoutManager(context));

        DividerItemDecoration itemDecor =
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecor.setDrawable(ContextCompat.getDrawable(context, R.drawable.separator));
        usersView.addItemDecoration(itemDecor);

        UsersAdapter.OnUserClickListener onUserClickListener = new UsersAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(User user) {
                if (user.getActive()) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra(ProfileActivity.USER_ID, user);
                    startActivity(intent);
                } else {
                    Toast.makeText(context, user.getName() + " is inactive",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        usersAdapter = new UsersAdapter(onUserClickListener, context);
        usersView.setAdapter(usersAdapter);
    }


    private void fillRecyclerView() {
        List<User> userList;
        if (data.isEmpty()) {
            loadUsersFromJson();
        } else {
            //get data from cache
            userList = data.getUsers();
            usersAdapter.setItems(userList);
        }
    }


    private void loadUsersFromJson() {
        JsonParserTask jsonParserTask = new JsonParserTask(this);
        jsonParserTask.execute();
    }


    public UsersAdapter getUsersAdapter() {
        return usersAdapter;
    }
}

