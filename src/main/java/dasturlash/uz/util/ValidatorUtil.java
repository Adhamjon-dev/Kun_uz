package dasturlash.uz.util;

import java.util.regex.Pattern;

public class ValidatorUtil {
    // Email regex pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    // Uzbekistan-style phone number regex pattern (+998xxxxxxxxx)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+998\\d{9}$"
    );

    public static boolean isEmail(String input) {
        return EMAIL_PATTERN.matcher(input).matches();
    }

    public static boolean isPhoneNumber(String input) {
        return PHONE_PATTERN.matcher(input).matches();
    }
}