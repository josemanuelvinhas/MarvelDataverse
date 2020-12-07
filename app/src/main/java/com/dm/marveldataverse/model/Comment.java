package com.dm.marveldataverse.model;

import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.ValidationException;

import java.util.regex.Pattern;

public class Comment {

    private long comment_id;
    private String user;
    private String comment;
    private long character;

    public Comment(long comment_id, String user, String comment, long character) {
        this.comment_id = comment_id;
        this.user = user;
        this.comment = comment;
        this.character = character;
    }

    public Comment(String user, String comment, long character) {
        this.user = user;
        this.comment = comment;
        this.character = character;
    }

    public Comment(){

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getComment_id() {
        return comment_id;
    }

    public void setComment_id(long comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getCharacter() {
        return character;
    }

    public void setCharacter(long character) {
        this.character = character;
    }


    public void validateComment() throws ValidationException {
        validateComment(this.comment);
    }


    public static void validateComment(String comment) throws ValidationException {
        final String regexp = "[a-zA-Z0-9ÑñÁáÉéÍíÓóÚúÜü'.,;() -]{1,255}";
        if (!Pattern.matches(regexp, comment)) {
            throw new ValidationException("Validation Comment Error", R.string.comment_invalid);
        }
    }



}