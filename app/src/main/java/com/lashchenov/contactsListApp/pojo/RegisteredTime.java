package com.lashchenov.contactsListApp.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RegisteredTime {
    private static final String PROFILE_RESPONSE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String REGISTERED_TIME_FORMAT = "HH:mm dd.MM.yy";

    public static String getFormattedDate(String rawDate) {
        SimpleDateFormat utcFormat = new SimpleDateFormat(PROFILE_RESPONSE_FORMAT, Locale.ROOT);
        SimpleDateFormat displayedFormat = new SimpleDateFormat(REGISTERED_TIME_FORMAT, Locale.getDefault());
        try {
            Date date = utcFormat.parse(rawDate);
            return displayedFormat.format(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
