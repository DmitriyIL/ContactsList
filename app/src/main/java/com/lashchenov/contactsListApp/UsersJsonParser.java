package com.lashchenov.contactsListApp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.lashchenov.contactsListApp.pojo.EyeColor;
import com.lashchenov.contactsListApp.pojo.FavoriteFruit;
import com.lashchenov.contactsListApp.pojo.RegisteredTime;
import com.lashchenov.contactsListApp.pojo.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersJsonParser {

    public List<User> parseUsers() {
        URL url = null;
        try {
            url = new URL("https://www.dropbox.com/s/s8g63b149tnbg8x/users.json?dl=1");
        } catch (java.net.MalformedURLException e) {
            e.printStackTrace();
        }
        if (url == null) return Collections.emptyList();

        String strJson = readJsonFromUrl(url);
        return parseJsonToUsersList(strJson);
    }


    private String readJsonFromUrl(URL url) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String ls = System.getProperty("line.separator");
            String tempStr = "";
            while ((tempStr = in.readLine()) != null) {
                sb.append(tempStr).append(ls);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }




    private List<User> parseJsonToUsersList(String strJson) {
        Gson g = new GsonBuilder().registerTypeAdapter(User.class, new UsersJsonParser.UserDeserializer()).create();
        Type itemsListType = new TypeToken<List<User>>() {
        }.getType();
        List<User> users = g.fromJson(strJson, itemsListType);
        return users;
    }


    private static class UserDeserializer implements JsonDeserializer<User> {
        private final Map<Integer, User> UserCache = new HashMap<>();

        @Override
        public User deserialize(JsonElement jElem, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            final JsonObject jsonObject = jElem.getAsJsonObject();

            User user = getOrCreateUser(jsonObject.get("id").getAsInt());

            if (jsonObject.get("isActive") != null) {
                user.setActive(jsonObject.get("isActive").getAsBoolean());
                user.setName(jsonObject.get("name").getAsString());
                user.setEmail(jsonObject.get("email").getAsString());
                user.setAge(jsonObject.get("age").getAsInt());
                user.setPhone(jsonObject.get("phone").getAsString());
                user.setCompany(jsonObject.get("company").getAsString());
                user.setLatitude(jsonObject.get("latitude").getAsDouble());
                user.setLongitude(jsonObject.get("longitude").getAsDouble());
                user.setAbout(jsonObject.get("about").getAsString());

                String unFormattedTime = jsonObject.get("registered").getAsString();
                user.setRegistered(RegisteredTime.getFormattedDate(unFormattedTime));

                String fruitInStr = jsonObject.get("favoriteFruit").getAsString();
                user.setFavoriteFruit(FavoriteFruit.parseFruit(fruitInStr));

                String eyeColorInStr = jsonObject.get("eyeColor").getAsString();
                user.setEyeColor(EyeColor.parseColor(eyeColorInStr));

                User[] users = context.deserialize(jsonObject.get("friends"), User[].class);
                user.setFriends(new ArrayList<>(Arrays.asList(users)));
            }

            return user;
        }


        private User getOrCreateUser(final int id) {
            User user = UserCache.get(id);
            if (user == null) {
                user = new User();
                user.setId(id);
                UserCache.put(id, user);
            }
            return user;
        }
    }
}
