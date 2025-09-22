package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Lets a user choose which role to play after login.
 * Only Admin actually routes to a functional page; others go to a simple mock page.
 */
public class RoleSelectionPage {

    private DatabaseHelper db;   // database connection
    private String user;         // current username

    public RoleSelectionPage(DatabaseHelper d, String u) {
        db = d;
        user = u;
    }

    public void show(Stage stage) {
        // title text
        Label lblTitle = new Label("Choose a role to play:");

        // buttons for each role
        Button btnAdmin = new Button("Admin");
        Button btnStudent = new Button("Student");
        Button btnStaff = new Button("Staff");
        Button btnReviewer = new Button("Reviewer");
        Button btnBack = new Button("Back");

        // admin goes to the real admin page
        btnAdmin.setOnAction(e -> {
            AdminHomePage adminPage = new AdminHomePage(db);
            adminPage.show(stage);
        });

        // other roles go to fake role pages
        btnStudent.setOnAction(e -> {
            MockRolePage p = new MockRolePage(stage, db, user, "Student");
            p.show(stage);
        });
        btnStaff.setOnAction(e -> {
            MockRolePage p = new MockRolePage(stage, db, user, "Staff");
            p.show(stage);
        });
        btnReviewer.setOnAction(e -> {
            MockRolePage p = new MockRolePage(stage, db, user, "Reviewer");
            p.show(stage);
        });

        // back goes to welcome screen
        btnBack.setOnAction(e -> {
            WelcomeLoginPage w = new WelcomeLoginPage(db);
            w.show(stage, user);
        });

        // put all controls together
        VBox root = new VBox(10, lblTitle, btnAdmin, btnStudent, btnStaff, btnReviewer, btnBack);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-alignment: center;");

        // make scene and show it
        stage.setScene(new Scene(root, 800, 400));
        stage.setTitle("Select Role");
    }
}
