package com.dm.marveldataverse.model;

public class User {

    private String username;
    private String passwd;
    private String email;

    public User(String username, String passwd, String email) {
        this.username = username;
        this.passwd = passwd;
        this.email = email;
    }

    public User(String username, String passwd) {
        this.username = username;
        this.passwd = passwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}