package com.lashchenov.contactsListApp.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
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
    private TextView companyTextView;
    private TextView registeredTextView;
    private TextView aboutTextView;
    private ImageView eyeColorImageView;
    private ImageView favoriteFruitImageView;
    private RecyclerView friendsView;

    private TextView clickableEmail;
    private TextView clickablePhone;
    private TextView clickableLocation;

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initToolbar();
        initViewComponents();

        clickableEmail.setOnClickListener(this);
        clickablePhone.setOnClickListener(this);
        clickableLocation.setOnClickListener(this);

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
            case R.id.emailClickable:
                intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", user.getEmail(), null));
                break;
            case R.id.phoneClickable:
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + user.getPhone()));
                break;
            case R.id.locationClickable:
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
        companyTextView = findViewById(R.id.companyText);
        registeredTextView = findViewById(R.id.registeredText);
        aboutTextView = findViewById(R.id.aboutText);
        eyeColorImageView = findViewById(R.id.eyeColorImage);
        favoriteFruitImageView = findViewById(R.id.favoriteFruitImage);

        clickableEmail = findViewById(R.id.emailClickable);
        clickablePhone = findViewById(R.id.phoneClickable);
        clickableLocation = findViewById(R.id.locationClickable);

        initRecyclerView();
    }


    private void initRecyclerView() {
        final Context context = getBaseContext();

        friendsView = findViewById(R.id.usersRecyclerView);
        friendsView.setLayoutManager(new LinearLayoutManager(context));

        DividerItemDecoration itemDecor =
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        itemDecor.setDrawable(ContextCompat.getDrawable(context, R.drawable.separator));
        friendsView.addItemDecoration(itemDecor);

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
        friendsAdapter = new UsersAdapter(onUserClickListener, context);
        friendsView.setAdapter(friendsAdapter);
    }


    private void fillFriendsView(User user) {
        Collection<User> friends = user.getFriends();
        friendsAdapter.setItems(friends);
    }


    private void displayProfileInfo(User user) {
        nameTextView.setText(user.getName());
        ageTextView.setText(String.format(getString(R.string.YO), user.getAge()));
        companyTextView.setText(String.format(getString(R.string.company), user.getCompany()));
        registeredTextView.setText(String.format(
                getString(R.string.registered), user.getRegistered()));
        aboutTextView.setText(String.format(getString(R.string.about), user.getAbout()));

        clickableEmail.setText(highlightLink(user.getEmail()));
        clickablePhone.setText(highlightLink(user.getPhone()));
        clickableLocation.setText(highlightLink(user.getLatitude() + ", " + user.getLongitude()));

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


    private Spannable highlightLink(String str) {
        int spanConst = Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;
        Spannable text = new SpannableString(str);
        text.setSpan(new UnderlineSpan(), 0, str.length(), spanConst);
        int color = ContextCompat.getColor(this, R.color.linkColor);
        text.setSpan(new ForegroundColorSpan(color), 0, str.length(), spanConst);
        return text;
    }
}
