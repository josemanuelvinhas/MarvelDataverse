package com.dm.marveldataverse.model;

import com.dm.marveldataverse.R;

public class Fav {

    private long fav_id;
    private String user;
    private String character;

    public Fav(long fav_id, String user, String character) {
        this.fav_id = fav_id;
        this.user = user;
        this.character = character;
    }

    public long getFav_id() {
        return fav_id;
    }

    public void setFav_id(long fav_id) {
        this.fav_id = fav_id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

}