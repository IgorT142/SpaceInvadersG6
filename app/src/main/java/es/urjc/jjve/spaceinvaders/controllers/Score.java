package es.urjc.jjve.spaceinvaders.controllers;

import android.net.Uri;

public class Score {

    private int score;
    private String name;
    private Uri uri;

    public Score(String name, int score, Uri uri) {
        this.score = score;
        this.name = name;
        this.uri = uri;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
