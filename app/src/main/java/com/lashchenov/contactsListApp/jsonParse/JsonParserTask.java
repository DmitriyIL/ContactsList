package com.lashchenov.contactsListApp.jsonParse;

import android.content.Context;
import android.os.AsyncTask;

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
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParserTask extends AsyncTask<Void, Void, List<User>> {

    private WeakReference<Context> context;

    public JsonParserTask(Context context) {
        this.context = new WeakReference<>(context);
    }


    @Override
    protected List<User> doInBackground(Void... params) {
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
        Gson g = new GsonBuilder().registerTypeAdapter(User.class, new UserDeserializer()).create();
        Type itemsListType = new TypeToken<List<User>>() {}.getType();
        List<User> users = g.fromJson(strJson, itemsListType);
        return users;
    }


    private static class UserDeserializer implements JsonDeserializer<User> {
        private final Map<Integer, User> cache = new HashMap<>();

        @Override
        public User deserialize(JsonElement jElem, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            final JsonObject jsonObject = jElem.getAsJsonObject();

            final User user = getOrCreate(jsonObject.get("id").getAsInt());

            if (jsonObject.get("isActive") != null) {
                user.setState(jsonObject.get("isActive").getAsBoolean());
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


        private User getOrCreate(final int id) {
            User user = cache.get(id);
            if (user == null) {
                user = new User();
                user.setId(id);
                cache.put(id, user);
            }
            return user;
        }
    }


    @Override
    protected void onPostExecute(List<User> users) {
        super.onPostExecute(users);
        //TODO сохранение в SQL;
    }


    /*private void writeFile(String fileContent) {
        try {
            String fileName = "users.json";
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                    context.get().openFileOutput(fileName, context.get().MODE_PRIVATE)));
            bw.write(fileContent);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}

