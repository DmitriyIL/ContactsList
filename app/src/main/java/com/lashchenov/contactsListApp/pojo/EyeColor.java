package com.lashchenov.contactsListApp.pojo;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import com.lashchenov.contactsListApp.R;

public enum EyeColor {

    BROWN {
        @Override
        public int getId(Context context) {
            return ContextCompat.getColor(context, R.color.brownEye);
        }
    },
    GREEN {
        @Override
        public int getId(Context context) {
            return ContextCompat.getColor(context, R.color.greenEye);
        }
    },
    BLUE {
        @Override
        public int getId(Context context) {
            return ContextCompat.getColor(context, R.color.blueEye);
        }
    };

    public abstract int getId(Context context);

    public static EyeColor parseColor(String str) {
        switch (str) {
            case "blue":
                return EyeColor.BLUE;
            case "green":
                return EyeColor.GREEN;
            case "brown":
                return EyeColor.BROWN;
            default:
                throw new IllegalArgumentException("Color not defined: " + str);
        }
    }
}
