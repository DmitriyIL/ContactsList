package com.lashchenov.contactsListApp.pojo;

import com.lashchenov.contactsListApp.R;

public enum FavoriteFruit {

    APPLE(R.drawable.apple, 0),
    BANANA(R.drawable.banana, 1),
    STRAWBERRY(R.drawable.strawberry, 2);


    private final int drawableId;
    private final int id;

    FavoriteFruit(int drawableId, int id) {
        this.drawableId = drawableId;
        this.id = id;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public int getId() {
        return id;
    }

    public static FavoriteFruit parseFruit(String str) {
        switch (str) {
            case "apple":
                return FavoriteFruit.APPLE;
            case "banana":
                return FavoriteFruit.BANANA;
            case "strawberry":
                return FavoriteFruit.STRAWBERRY;
            default:
                throw new IllegalArgumentException("Fruit not defined");
        }
    }
}
