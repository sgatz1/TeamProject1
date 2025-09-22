package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Very simple forgot password page.
 * Lets user type username and a new password.
 */
public class ForgotPasswordPage {

    private DatabaseHelper db;

    public ForgotPasswordPage(DatabaseHelper db) {
        this.db = db;
    }

    public void show(Stage stage) {
        TextField userField = new TextField();
        userField.setPromptText("Enter username");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter new password");

        Button btnChange = new Button("Change Password");
        Label msg = new Label();

        btnChange.setOnAction(e -> {
            String uname = userField.getText().trim();
            String newPass = passField.getText().trim();

            if (uname.isEmpty() || newPass.isEmpty()) {
                msg.setText("Please fill both fields.");
                return;
            }

            if (newPass.length() < 8) {
                msg.setText("Password must be at least 8 characters.");
                return;
            }

            boolean ok = db.resetPassword(uname, newPass);
            if (ok) {
                msg.setText("Password updated. Go back and login.");
            } else {
                msg.setText("Could not update password.");
            }
        });

        Button btnBack = new Button("Back");
        btnBack.setOnAction(e -> new UserLoginPage(db).show(stage));

        VBox layout = new VBox(10, userField, passField, btnChange, msg, btnBack);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        stage.setScene(new Scene(layout, 400, 300));
        stage.setTitle("Forgot Password");
        stage.show();
    }
}
