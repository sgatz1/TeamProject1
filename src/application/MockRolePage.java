package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Simple placeholder page for non-admin roles.
 */
public class MockRolePage {

    private Stage stage;       // the window
    private DatabaseHelper db; // database helper
    private String user;       // the person logged in
    private String role;       // what role they have

    public MockRolePage(Stage s, DatabaseHelper d, String u, String r) {
        stage = s;
        db = d;
        user = u;
        role = r;
    }

    public void show(Stage unused) {
        // show what role this page is for
        Label lblTitle = new Label("Role: " + role);
        Label lblInfo = new Label("This is a placeholder screen for \"" + role + "\" role (Phase 1).");

        // buttons
        Button btnChangeRole = new Button("Change Role");
        Button btnBack = new Button("Back");

        // what the buttons do
        btnChangeRole.setOnAction(e -> {
            RoleSelectionPage rPage = new RoleSelectionPage(db, user);
            rPage.show(stage);
        });

        btnBack.setOnAction(e -> {
            WelcomeLoginPage w = new WelcomeLoginPage(db);
            w.show(stage, user);
        });

        // put everything into layout
        VBox root = new VBox(10, lblTitle, lblInfo, btnChangeRole, btnBack);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-alignment: center;");

        // set scene
        stage.setScene(new Scene(root, 800, 400));
        stage.setTitle(role + " Home");
    }
}
