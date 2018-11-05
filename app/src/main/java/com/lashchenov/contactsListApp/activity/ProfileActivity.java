package com.lashchenov.contactsListApp.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lashchenov.contactsListApp.R;
import com.lashchenov.contactsListApp.adapter.UsersAdapter;
import com.lashchenov.contactsListApp.pojo.EyeColor;
import com.lashchenov.contactsListApp.pojo.User;

import java.util.Collection;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String USER_ID = "userId";

    private ScrollView scrollView;
    private Toolbar toolbar;
    private UsersAdapter friendsAdapter;

    private TextView nameTextView;
    private TextView ageTextView;
    private TextView emailTextView;
    private TextView phoneTextView;
    private TextView companyTextView;
    private TextView locationTextView;
    private TextView registeredTextView;
    private TextView aboutTextView;
    private ImageView eyeColorImageView;
    private ImageView favoriteFruitImageView;
    private RecyclerView friendsRecyclerView;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initToolbar();
        initViewComponents();

        emailTextView.setOnClickListener(this);
        phoneTextView.setOnClickListener(this);
        locationTextView.setOnClickListener(this);

        user = (User) getIntent().getExtras().getSerializable(USER_ID);
        displayProfileInfo(user);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.emailText:
                intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ user.getEmail() });
                break;
            case R.id.phoneText:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + user.getPhone()));
                break;
            case R.id.locationText:
                Uri gmmIntentUri = Uri.parse("geo:"
                        + user.getLatitude() + "," + user.getLongitude());
                intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                break;
        }
        if (intent != null && intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    private void initToolbar() {
        toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void initViewComponents() {
        scrollView = findViewById(R.id.profileScrollView);
        scrollView.smoothScrollTo(0, 0);

        nameTextView = findViewById(R.id.nameText);
        ageTextView = findViewById(R.id.yoText);
        emailTextView = findViewById(R.id.emailText);
        phoneTextView = findViewById(R.id.phoneText);
        companyTextView = findViewById(R.id.companyText);
        locationTextView = findViewById(R.id.locationText);
        registeredTextView = findViewById(R.id.registeredText);
        aboutTextView = findViewById(R.id.aboutText);
        eyeColorImageView = findViewById(R.id.eyeColorImage);
        favoriteFruitImageView = findViewById(R.id.favoriteFruitImage);

        initFriendsRecyclerView();
    }


    private void initFriendsRecyclerView() {
        friendsRecyclerView = findViewById(R.id.usersRecyclerView);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        UsersAdapter.OnUserClickListener onUserClickListener = new UsersAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(User user) {
                if (user.getActive()) {
                    Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                    intent.putExtra(ProfileActivity.USER_ID, user);
                    startActivity(intent);
                } else {
                    Toast.makeText(ProfileActivity.this, user.getName() + " is inactive",
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        friendsAdapter = new UsersAdapter(onUserClickListener, getBaseContext());
        friendsRecyclerView.setAdapter(friendsAdapter);
    }


    private void fillFriendsView(User user) {
        Collection<User> friends = user.getFriends();
        friendsAdapter.setItems(friends);
    }


    private void displayProfileInfo(User user) {
        nameTextView.setText(user.getName());
        ageTextView.setText(String.format(getString(R.string.YO), user.getAge()));
        emailTextView.setText(String.format(getString(R.string.email), user.getEmail()));
        phoneTextView.setText(String.format(getString(R.string.phone), user.getPhone()));
        companyTextView.setText(String.format(getString(R.string.company), user.getCompany()));
        locationTextView.setText(String.format(
                getString(R.string.location), user.getLatitude(), user.getLongitude()));
        registeredTextView.setText(String.format(
                getString(R.string.registered), user.getRegistered()));
        aboutTextView.setText(String.format(getString(R.string.about), user.getAbout()));

        Drawable background = eyeColorImageView.getBackground();
        if (background instanceof GradientDrawable) {
            EyeColor eyeColor = user.getEyeColor();
            int drawableId = ContextCompat.getColor(this, eyeColor.getDrawableId());
            ((GradientDrawable) background).setColor(drawableId);
        }

        int fruitId = user.getFavoriteFruit().getDrawableId();
        favoriteFruitImageView.setBackgroundResource(fruitId);

        fillFriendsView(user);
    }
}
