package org.example.validator;

import org.example.exceptions.InvalidFormatException;
import org.example.exceptions.NullValueException;
import org.example.model.Profile;

public class Validations {

    public static void nullCheck(Profile profile) {
        if (profile.getFullName().isEmpty()) {
            throw new NullValueException("Fill your full name");
        } else if (profile.getGender() == null) {
            throw new NullValueException("Fill your gender");
        } else if (profile.getProfession().isEmpty() ) {
            throw new NullValueException("Fill your profession");
        } else if (profile.getProfession().length() > 15) {
            throw new InvalidFormatException("Please enter valid profession");
        } else if (profile.getHobby().isEmpty()) {
            throw new NullValueException("Fill your interests");
        } else if (profile.getHobby().length() > 15) {
            throw new InvalidFormatException("Please enter valid values");
        } else if (profile.getFavoriteColour().isEmpty()) {
            throw new NullValueException("Fill your fav colour");
        } else if (profile.getFavoriteColour().length() > 10) {
            throw new InvalidFormatException("Please enter valid colour value");
        } else if (profile.getFavoritePet().isEmpty()) {
            throw new NullValueException("Fill your fev pet type");
        } else if (profile.getFavoritePet().length() > 10) {
            throw new InvalidFormatException("Please enter valid value");
        } else if (profile.getFoodType() == null) {
            throw new NullValueException("Fill your food type");
        } else if (profile.getThoughts() == null) {
            throw new NullValueException("Fill your response");
        }
    }
}
