package com.example.jinming.gamestopdemo.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jinming on 11/25/17.
 */

public class Sentence {
    String title;
    String content;
    private static List<Sentence> res;

    static {
        res = new ArrayList<>();
        String[] titles = UtilArray.getTitles();
        String[] content = UtilArray.getContent();
        for (int i = 0; i < titles.length; i++) {
            Sentence s = new Sentence(titles[i], content[i]);
            res.add(s);
        }
    }

    public Sentence(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public static List<Sentence> get() {
        return res;
    }
}
