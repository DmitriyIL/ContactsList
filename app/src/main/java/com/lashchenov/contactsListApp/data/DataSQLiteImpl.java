package com.lashchenov.contactsListApp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lashchenov.contactsListApp.pojo.EyeColor;
import com.lashchenov.contactsListApp.pojo.FavoriteFruit;
import com.lashchenov.contactsListApp.pojo.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataSQLiteImpl implements Data {

    private Context context;
    private UsersDBHelper usersDBHelper;

    private HashMap<Integer, User> usersCache = new HashMap<>();


    public DataSQLiteImpl(Context context) {
        this.context = context;
        usersDBHelper = UsersDBHelper.getInstance(context);
    }


    public void setUsers(List<User> userList) {
        SQLiteDatabase db = usersDBHelper.getWritableDatabase();

        clearData(db);

        for (User user : userList) {
            ContentValues userValues = new ContentValues();


            userValues.put(UserContract.UserEntry.ID, user.getId());
            userValues.put(UserContract.UserEntry.COLUMN_NAME, user.getName());
            userValues.put(UserContract.UserEntry.COLUMN_AGE, user.getAge());
            userValues.put(UserContract.UserEntry.COLUMN_EMAIL, user.getEmail());
            userValues.put(UserContract.UserEntry.COLUMN_PHONE, user.getPhone());
            userValues.put(UserContract.UserEntry.COLUMN_COMPANY, user.getCompany());
            userValues.put(UserContract.UserEntry.COLUMN_EYE_COLOR, user.getEyeColor().getId());
            userValues.put(UserContract.UserEntry.COLUMN_FRUIT, user.getFavoriteFruit().getId());
            userValues.put(UserContract.UserEntry.COLUMN_LATITUDE, user.getLatitude());
            userValues.put(UserContract.UserEntry.COLUMN_LONGITUDE, user.getLongitude());
            userValues.put(UserContract.UserEntry.COLUMN_REGISTERED, user.getRegistered());
            userValues.put(UserContract.UserEntry.COLUMN_ABOUT, user.getAbout());

            int activeInt = (user.getActive()) ? 1 : 0;
            userValues.put(UserContract.UserEntry.COLUMN_ACTIVE, activeInt);

            String friendsId = friendsListToString(user.getFriends());
            userValues.put(UserContract.UserEntry.COLUMN_FRIENDS, friendsId);

            db.insert(UserContract.UserEntry.TABLE_NAME, null, userValues);
        }
    }


    public List<User> getUsers() {
        SQLiteDatabase db = usersDBHelper.getReadableDatabase();
        Cursor cursor = db.query(
                UserContract.UserEntry.TABLE_NAME,
                getProjection(),       // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,
                null,
                null);
        if (cursor == null) throw new IllegalArgumentException("database empty");
        List<User> userList = getUsersByCursor(cursor);
        cursor.close();
        return userList;
    }


    public void clearData(SQLiteDatabase db) {
        db.delete(UserContract.UserEntry.TABLE_NAME, null, null);
    }


    private List<User> getUsersByCursor(Cursor cursor) {
        List<User> userList = new ArrayList<>();

        // Узнаем индекс каждого столбца
        int idColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.ID);
        int activeColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_ACTIVE);
        int nameColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_NAME);
        int ageColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_AGE);
        int emailColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_EMAIL);
        int phoneColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_PHONE);
        int companyColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_COMPANY);
        int eyeColorColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_EYE_COLOR);
        int fruitColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_FRUIT);
        int latitudeColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_LATITUDE);
        int longitudeColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_LONGITUDE);
        int registeredColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_REGISTERED);
        int aboutColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_ABOUT);
        int friendsColumnIndex = cursor.getColumnIndex(UserContract.UserEntry.COLUMN_FRIENDS);


        while (cursor.moveToNext()) {
            User user = getOrCreateUser(cursor.getInt(idColumnIndex));

            user.setName(cursor.getString(nameColumnIndex));
            user.setAge(cursor.getInt(ageColumnIndex));
            user.setEmail(cursor.getString(emailColumnIndex));
            user.setPhone(cursor.getString(phoneColumnIndex));
            user.setCompany(cursor.getString(companyColumnIndex));
            user.setLatitude(cursor.getDouble(latitudeColumnIndex));
            user.setLongitude(cursor.getDouble(longitudeColumnIndex));
            user.setRegistered(cursor.getString(registeredColumnIndex));
            user.setAbout(cursor.getString(aboutColumnIndex));

            Boolean isActive = 1 == cursor.getInt(activeColumnIndex);
            user.setActive(isActive);

            EyeColor eyeColor = EyeColor.values()[cursor.getInt(eyeColorColumnIndex)];
            user.setEyeColor(eyeColor);

            FavoriteFruit favoriteFruit = FavoriteFruit.values()[cursor.getInt(fruitColumnIndex)];
            user.setFavoriteFruit(favoriteFruit);

            String strFriends = cursor.getString(friendsColumnIndex);
            user.setFriends(stringToFriendsList(strFriends));

            userList.add(user);
        }

        usersCache.clear();
        return userList;
    }


    public Boolean isEmpty() {
        Cursor cursor = usersDBHelper.getReadableDatabase().query(
                UserContract.UserEntry.TABLE_NAME,
                new String[]{"_ID"},
                null,
                null,
                null,
                null,
                null);
        if (cursor == null) return true;
        if (cursor.moveToFirst()) return false;
        cursor.close();
        return true;
    }


    private List<User> stringToFriendsList(String str) {
        List<User> userList = new ArrayList<>();
        String[] strFriendsArr = str.split("\\s+");
        for (int i = 0; i < strFriendsArr.length; i++) {
            int friendId = Integer.parseInt(strFriendsArr[i]);
            userList.add(getOrCreateUser(friendId));
        }
        return userList;
    }


    private String friendsListToString(List<User> userList) {
        StringBuilder result = new StringBuilder();
        for (User user : userList) {
            result.append(user.getId()).append(" ");
        }
        return result.toString();
    }


    private User getOrCreateUser(int id) {
        User user = usersCache.get(id);
        if (user == null) {
            user = new User();
            user.setId(id);
            usersCache.put(id, user);
        }
        return user;
    }


    private static String[] getProjection() {
        return new String[] {
                UserContract.UserEntry.ID,
                UserContract.UserEntry.COLUMN_ACTIVE,
                UserContract.UserEntry.COLUMN_NAME,
                UserContract.UserEntry.COLUMN_AGE,
                UserContract.UserEntry.COLUMN_EMAIL,
                UserContract.UserEntry.COLUMN_PHONE,
                UserContract.UserEntry.COLUMN_COMPANY,
                UserContract.UserEntry.COLUMN_EYE_COLOR,
                UserContract.UserEntry.COLUMN_FRUIT,
                UserContract.UserEntry.COLUMN_LATITUDE,
                UserContract.UserEntry.COLUMN_LONGITUDE,
                UserContract.UserEntry.COLUMN_REGISTERED,
                UserContract.UserEntry.COLUMN_ABOUT,
                UserContract.UserEntry.COLUMN_FRIENDS};
    }
}
