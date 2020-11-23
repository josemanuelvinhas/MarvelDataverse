package com.dm.marveldataverse.model;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.ValidationException;

import java.util.regex.Pattern;

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

    public User() {
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

    public void validateForRegister() throws ValidationException {
        validateUsername(this.username);
        validateEmail(this.email);
        validatePasswd(this.passwd);
    }

    public void validateForLogin() throws ValidationException {
        validateUsername(this.username);
        validatePasswd(this.passwd);
    }

    public static void validateUsername(String username) throws ValidationException {
        final String regexp = "[a-zA-Z0-9]{3,20}";
        if (!Pattern.matches(regexp, username)) {
            throw new ValidationException("Validation Login Error", R.string.username_invalid);
        }
    }

    public static void validatePasswd(String passwd) throws ValidationException {
        final String regexp = "[a-zA-Z0-9]{3,20}";
        if (!Pattern.matches(regexp, passwd)) {
            throw new ValidationException("Validation Login Error", R.string.passwd_invalid);
        }
    }

    public static void validateEmail(String email) throws ValidationException {
        final String regexp = "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";
        if (!Pattern.matches(regexp, email)) {
            throw new ValidationException("Validation Login Error", R.string.email_invalid);
        }
    }


}