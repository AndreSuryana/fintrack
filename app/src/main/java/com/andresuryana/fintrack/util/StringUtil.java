package com.andresuryana.fintrack.util;

import java.util.regex.Pattern;

public class StringUtil {

    private static final String EMAIL_PATTERN = "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+$";
    private static final Integer PASSWORD_MIN_LENGTH = 8;

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password.length() >= PASSWORD_MIN_LENGTH;
    }

    public static boolean containsOnlyLettersWithSpaces(String input) {
        return input.matches("[a-zA-Z\\s]+");
    }
}
