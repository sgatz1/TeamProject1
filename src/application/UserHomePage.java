package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Optional user profile/home screen:
 * - shows current email
 * - allows basic email update (uses db.getUserEmail / db.updateUserEmail)
 */
public class UserHomePage {

    private Stage stage;        // window
    private DatabaseHelper db;  // database
    private String user;        // username

    public UserHomePage(Stage s, DatabaseHelper d, String u) {
        stage = s;
        db = d;
        user = u;
    }

    public void show(Stage unused) {
        // labels and text fields
        Label lblTitle = new Label("User Home");
        Label lblEmail = new Label("Current email: " + safe(db.getUserEmail(user)));
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("New email");

        // buttons
        Button btnUpdate = new Button("Update Email");
        Button btnBack = new Button("Back");

        Label lblStatus = new Label();

        // what happens when update is clicked
        btnUpdate.setOnAction(e -> {
            String newEmail = txtEmail.getText();
            if (newEmail == null || newEmail.trim().isEmpty()) {
                lblStatus.setText("Enter a valid email.");
                return;
            }
            boolean ok = db.updateUserEmail(user, newEmail.trim());
            if (ok) {
                lblStatus.setText("Email updated.");
                lblEmail.setText("Current email: " + safe(db.getUserEmail(user)));
                txtEmail.clear();
            } else {
                lblStatus.setText("Update failed.");
            }
        });

        // what happens when back is clicked
        btnBack.setOnAction(e -> {
            WelcomeLoginPage w = new WelcomeLoginPage(db);
            w.show(stage, user);
        });

        // layout
        VBox root = new VBox(10, lblTitle, lblEmail, txtEmail, btnUpdate, lblStatus, btnBack);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-alignment: center;");

        // show scene
        stage.setScene(new Scene(root, 800, 400));
        stage.setTitle("User Home");
    }

    // make sure we don’t print null
    private String safe(String s) {
        return (s == null ? "(none)" : s);
    }
}
