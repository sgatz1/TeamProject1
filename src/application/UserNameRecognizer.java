package application;

/**
 * This class was provided to validate usernames.
 * I simplified it to make it easier to use and understand.
 */
public class UserNameRecognizer {

    public static String checkForValidUserName(String input) {
        // make sure it is not empty
        if (input == null || input.isEmpty()) {
            return "Username cannot be empty.";
        }

        // check length too short
        if (input.length() < 4) {
            return "Username must be at least 4 characters long.";
        }

        // check length too long
        if (input.length() > 16) {
            return "Username cannot be more than 16 characters long.";
        }

        // must start with a letter
        if (!Character.isLetter(input.charAt(0))) {
            return "Username must start with a letter.";
        }

        // only allow letters, numbers, underscore, dash, dot
        if (!input.matches("^[a-zA-Z][a-zA-Z0-9_.-]*$")) {
            return "Username contains invalid characters.";
        }

        // if nothing wrong return empty string
        return "";
    }
}
