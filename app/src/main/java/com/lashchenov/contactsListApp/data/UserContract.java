package com.lashchenov.contactsListApp.data;

import android.provider.BaseColumns;

public final class UserContract {

    private UserContract() {
    }

    public static final class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";

        public static final String _ID = BaseColumns._ID;
        public static final String ID = "id";
        public static final String COLUMN_ACTIVE = "isActive";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PHONE = "phone";
        public static final String COLUMN_COMPANY = "company";
        public static final String COLUMN_EYE_COLOR = "eyeColor";
        public static final String COLUMN_FRUIT = "favoriteFruit";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_REGISTERED = "registered";
        public static final String COLUMN_ABOUT = "about";
        public static final String COLUMN_FRIENDS = "friends";
    }
}
