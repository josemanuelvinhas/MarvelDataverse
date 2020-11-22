package com.dm.marveldataverse.model;


import com.dm.marveldataverse.R;
import com.dm.marveldataverse.core.ValidationException;

import java.util.regex.Pattern;

public class Character {

    private String name;
    private String description;
    //Posible foto

    public Character(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void validateForCreate() throws ValidationException {
        validateName(this.name);
        validateDescription(this.description);

    }

    public void validateForUpdate() throws ValidationException {
        validateName(this.name);
        validateDescription(this.description);
    }

    public void validateForSearch() throws ValidationException {
        validateSearch(this.name);
    }

    public static void validateName(String name) throws ValidationException {
        final String regexp = "[a-zA-Z0-9-.]{3,20}";
        if (!Pattern.matches(regexp, name)) {
            throw new ValidationException("Validation Character Error", R.string.name_invalid);
        }
    }

    public static void validateDescription(String description) throws ValidationException {
        final String regexp = "[a-zA-Z0-9]{3,255}"; //TODO Ver tamaño maximo en sqlite del string
        if (!Pattern.matches(regexp, description)) {
            throw new ValidationException("Validation Character Error", R.string.description_invalid);
        }
    }

    public static void validateSearch(String description) throws ValidationException {
        final String regexp = "[a-zA-Z0-9-.]{1,20}";
        if (!Pattern.matches(regexp, description)) {
            throw new ValidationException("Character Search Validation Error", R.string.search_invalid);
        }
    }



}