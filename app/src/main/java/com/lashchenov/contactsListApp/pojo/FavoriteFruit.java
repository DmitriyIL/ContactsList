package com.lashchenov.contactsListApp.pojo;

import com.lashchenov.contactsListApp.R;

public enum FavoriteFruit {

    APPLE {
        @Override
        public int getFruitId() {
            return R.drawable.apple;
        }
    },
    BANANA {
        @Override
        public int getFruitId() {
            return R.drawable.banana;
        }
    },
    STRAWBERRY {
        @Override
        public int getFruitId() {
            return R.drawable.strawberry;
        }
    };

    public abstract int getFruitId();

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
