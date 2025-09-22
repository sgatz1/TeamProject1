package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// this screen is only used once to make the very first admin
public class AdminSetupPage {

    private DatabaseHelper db; // lets me talk to the database

    public AdminSetupPage(DatabaseHelper d) {
        db = d;
    }

    public void show(Stage stage) {
        // main layout for the page
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment:center; -fx-padding:20;");

        // text and input boxes
        Label lbl = new Label("Set up the initial Admin account");

        TextField txtUser = new TextField();
        txtUser.setPromptText("Enter admin username");

        PasswordField txtPass = new PasswordField();
        txtPass.setPromptText("Enter admin password");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Enter admin email");

        Button btnCreate = new Button("Create Admin");
        Label lblStatus = new Label();

        // button makes the admin when clicked
        btnCreate.setOnAction(e -> {
            String u = txtUser.getText().trim();
            String p = txtPass.getText().trim();
            String em = txtEmail.getText().trim();

            // make sure all fields are filled
            if (u.isEmpty() || p.isEmpty() || em.isEmpty()) {
                lblStatus.setText("All fields required.");
                return;
            }

            // try to add the new admin user
            boolean ok = db.register(u, p, em);
            if (ok) {
                db.addRole(u, "admin");
                lblStatus.setText("Admin created. Please login again.");
                SetupLoginSelectionPage next = new SetupLoginSelectionPage(db);
                next.show(stage);
            } else {
                lblStatus.setText("Failed to create admin.");
            }
        });

        // put everything into the layout
        layout.getChildren().addAll(lbl, txtUser, txtPass, txtEmail, btnCreate, lblStatus);

        // show the window
        stage.setScene(new Scene(layout, 800, 400));
        stage.setTitle("Admin Setup");
    }
}
