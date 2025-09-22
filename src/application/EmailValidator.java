package application;

/**
 * A simple helper class to check if an email looks valid.
 * This is just a basic check for Phase 1.
 */
public class EmailValidator {

    public static boolean isValidEmail(String email) {
        // check if it is null or empty first
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        // need to have @ somewhere
        if (!email.contains("@")) {
            return false;
        }
        // after the @ we also want a dot .
        int atPos = email.indexOf('@');
        String afterAt = email.substring(atPos);
        if (!afterAt.contains(".")) {
            return false;
        }
        // if it made it here then it seems fine
        return true;
    }
}
