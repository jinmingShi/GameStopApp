package com.example.jinming.gamestopdemo.util;

import android.media.Image;

import com.example.jinming.gamestopdemo.R;

/**
 * Created by Jinming on 11/24/17.
 */

public class UtilArray {
    public static final int[] images = new int[]{R.drawable.login,
                                    R.drawable.power,
                                    R.drawable.news,
                                    R.drawable.message,
                                    R.drawable.setting,
                                    R.drawable.about};
    private static final String[] names = {"SIGN IN", "POWERUP REWARDS",
                                           "GAME INFORMER NEWS", "MESSAGES",
                                           "SETTINGS", "ABOUT/FEEDBACK"};

    private static final String[] titles = {"MY SAVED SEARCHES", "COMING SOON", "GAMES", "CONSOLES", "ELECTRONICS", "ACCESSORIES"};

    private static final String[] content = {"", "coming soon coming soon coming soon", "games games games games games",
                                             "consoles consoles consoles consoles", "electronics electronics electronics",
                                             "accessories accessories accessories"};

    public static int[] getImages() {
        return images;
    }

    public static String[] getNames() {
        return names;
    }

    public static String[] getTitles() {
        return titles;
    }

    public static String[] getContent() {
        return content;
    }
}
