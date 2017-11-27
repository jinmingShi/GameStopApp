package com.example.jinming.gamestopdemo.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jinming on 11/24/17.
 */

public class Item {

    private int image;
    private String name;
    private static List<Item> res;
    static {
        res = new ArrayList<>();
        int[] images = UtilArray.getImages();
        String[] names = UtilArray.getNames();
        for (int i = 0; i < images.length; i++) {
            Item item = new Item(images[i], names[i]);
            res.add(item);
        }
    }
    public Item(int imageView, String name) {
        this.image = imageView;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageView(int imageView) {
        this.image = imageView;
    }

    public int getImageView() {
        return image;
    }

    public String getName() {
        return name;
    }

    public static List<Item> get() {
        return res;
    }
}

