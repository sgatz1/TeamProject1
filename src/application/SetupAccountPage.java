package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// this page lets someone make a new account using an invite code
public class SetupAccountPage {

    private DatabaseHelper db; // connection to database

    public SetupAccountPage(DatabaseHelper d) {
        db = d;
    }

    public void show(Stage primaryStage) {
        // text fields for user info
        TextField txtUser = new TextField();
        txtUser.setPromptText("Enter userName");
        txtUser.setMaxWidth(250);

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Enter Password");
        txtPass.setMaxWidth(250);

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Enter Email");
        txtEmail.setMaxWidth(250);

        TextField txtCode = new TextField();
        txtCode.setPromptText("Enter InvitationCode");
        txtCode.setMaxWidth(250);

        // error label and setup button
        Label lblError = new Label();
        lblError.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        Button btnSetup = new Button("Setup");

        // what happens when user clicks setup
        btnSetup.setOnAction(a -> {
            String u = txtUser.getText().trim();
            String p = txtPass.getText().trim();
            String em = txtEmail.getText().trim();
            String c = txtCode.getText().trim();

            // make sure nothing is empty
            if (u.isEmpty() || p.isEmpty() || em.isEmpty() || c.isEmpty()) {
                lblError.setText("All fields required.");
                return;
            }

            // very simple password check
            if (p.length() < 8) {
                lblError.setText("Invalid password: must be at least 8 chars.");
                return;
            }

            // check if code is valid
            if (!db.validateInvitationCode(c)) {
                lblError.setText("Please enter a valid invitation code.");
                return;
            }

            // try to register user
            boolean ok = db.register(u, p, em);
            if (ok) {
                SetupLoginSelectionPage next = new SetupLoginSelectionPage(db);
                next.show(primaryStage);
            } else {
                lblError.setText("Failed to register user.");
            }
        });

        // main layout
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(txtUser, txtPass, txtEmail, txtCode, btnSetup, lblError);

        // show page
        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Account Setup");
        primaryStage.show();
    }
}
