package com.dm.marveldataverse.model;

public class Fav {

    private long fav_id;
    private String user;
    private long character;

    public Fav(long fav_id, String user, long character) {
        this.fav_id = fav_id;
        this.user = user;
        this.character = character;
    }
    public Fav(String user, long character) {
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

    public long getCharacter() {
        return character;
    }

    public void setCharacter(long character) {
        this.character = character;
    }

}