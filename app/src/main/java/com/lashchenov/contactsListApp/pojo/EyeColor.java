package com.lashchenov.contactsListApp.pojo;

import com.lashchenov.contactsListApp.R;

public enum EyeColor {

    BROWN(R.color.brownEye, 0),
    GREEN(R.color.greenEye, 1),
    BLUE(R.color.blueEye, 2);

    private final int drawableId;
    private final int id;

    EyeColor(int drawableId, int id) {
        this.drawableId = drawableId;
        this.id = id;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public int getId() {
        return id;
    }

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
