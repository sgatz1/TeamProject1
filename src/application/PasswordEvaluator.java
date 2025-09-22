package application;

// this class checks if a password has the needed stuff in it
public class PasswordEvaluator {

    public static String evaluatePassword(String input) {
        // check if the password is empty or too short
        if (input == null || input.length() < 8) {
            return "Long Enough; ";
        }

        // flags for what we find
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        // go through every character
        for (char c : input.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                hasSpecial = true;
            }
        }

        // build the result message
        String result = "";
        if (!hasUpper) result += "Upper case; ";
        if (!hasLower) result += "Lower case; ";
        if (!hasDigit) result += "Numeric digits; ";
        if (!hasSpecial) result += "Special character; ";

        return result;
    }
}
