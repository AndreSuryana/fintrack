package com.andresuryana.fintrack.util;

import androidx.annotation.Nullable;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class StringUtil {

    private static final String EMAIL_PATTERN = "^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+$";
    private static final Integer PASSWORD_MIN_LENGTH = 8;
    private static final String DEFAULT_DATE_PATTERN = "d MMM yyyy";
    private static final String DEFAULT_PROFILE_IMAGE_URL = "https://ui-avatars.com/api/?name=%s&size=128&background=C8E6C9&color=388E3C";

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

    public static String formatRupiah(long value) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        format.setMaximumFractionDigits(0);
        return format.format(value);
    }

    public static long parseRupiah(String rupiah) {
        if (rupiah.isEmpty()) return 0;

        // Remove non-numeric characters from the input string
        String amount = rupiah.replaceAll("[^\\d]", "");

        return Long.parseLong(amount);
    }

    public static String formatDate(Date date, @Nullable String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern != null ? pattern : DEFAULT_DATE_PATTERN, Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String getProfileImageUrl(@Nullable String name) {
        if (name == null) return null;
        return String.format(DEFAULT_PROFILE_IMAGE_URL, name.replaceAll("\\s", "+"));
    }
}
