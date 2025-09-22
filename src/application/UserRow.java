package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/** Small table row holder for Admin table. */
public class UserRow {

    // properties for user info
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty role = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();

    // make a row with username, role, and email
    public UserRow(String u, String r, String e) {
        username.set(u);
        role.set(r);
        email.set(e);
    }

    // getters and property access
    public String getUsername() {
        return username.get();
    }
    public StringProperty usernameProperty() {
        return username;
    }

    public String getRole() {
        return role.get();
    }
    public StringProperty roleProperty() {
        return role;
    }

    public String getEmail() {
        return email.get();
    }
    public StringProperty emailProperty() {
        return email;
    }
}
