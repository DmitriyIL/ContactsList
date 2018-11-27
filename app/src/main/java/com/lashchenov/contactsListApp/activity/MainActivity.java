package com.lashchenov.contactsListApp.activity;

import android.arch.lifecycle.ViewModelProviders;
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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lashchenov.contactsListApp.R;
import com.lashchenov.contactsListApp.adapter.UsersAdapter;
import com.lashchenov.contactsListApp.data.Data;
import com.lashchenov.contactsListApp.data.DataSQLiteImpl;
import com.lashchenov.contactsListApp.model.MainModel;
import com.lashchenov.contactsListApp.pojo.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    private MainModel mainModel;

    @BindView(R.id.profileRecyclerView) RecyclerView usersView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.progressBar) ProgressBar progressBarView;

    private UsersAdapter usersAdapter;
    private Data data;

//check push
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = new DataSQLiteImpl(this);

        ButterKnife.bind(this);

        mainModel = ViewModelProviders.of(this).get(MainModel.class);
        mainModel.getDataLive().observe(this, data -> {
            turnOffWaiting(data);
        });


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
            turnOnWaiting();
            mainModel.loadUsers();
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

        usersView = findViewById(R.id.profileRecyclerView);
        usersView.setLayoutManager(new LinearLayoutManager(context));

        DividerItemDecoration itemDecor =
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecor.setDrawable(ContextCompat.getDrawable(context, R.drawable.separator));
        usersView.addItemDecoration(itemDecor);

        UsersAdapter.OnUserClickListener onUserClickListener = user -> {
            if (user.getActive()) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra(ProfileActivity.USER_ID, user);
                startActivity(intent);
            } else {
                Toast.makeText(context, user.getName() + " is inactive",
                        Toast.LENGTH_SHORT).show();
            }
        };
        usersAdapter = new UsersAdapter(onUserClickListener, context);
        usersView.setAdapter(usersAdapter);
    }


    private void fillRecyclerView() {
        List<User> userList;
        if (data.isEmpty()) {
            turnOnWaiting();
            mainModel.loadUsers();
        } else {
            //get data from cache
            userList = data.getUsers();
            usersAdapter.setItems(userList);
        }
    }


    private void turnOnWaiting() {
        usersAdapter.clearItems();
        progressBarView.setVisibility(View.VISIBLE);
    }


    private void turnOffWaiting(List<User> newUsers) {
        data.setUsers(newUsers);
        usersAdapter.setItems(newUsers);
        progressBarView.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "Users loaded", Toast.LENGTH_SHORT).show();
    }


    public UsersAdapter getUsersAdapter() {
        return usersAdapter;
    }
}

