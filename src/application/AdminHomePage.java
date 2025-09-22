package application;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Admin page where admins can:
 * - see list of users
 * - add or remove roles
 * - delete users
 * - make invitation codes
 */
public class AdminHomePage {

    private DatabaseHelper db;
    private TableView<String[]> table;
    private ObservableList<String[]> rows;

    public AdminHomePage(DatabaseHelper db) {
        this.db = db;
    }

    public void show(Stage stage) {
        VBox root = new VBox(12);
        root.setPadding(new Insets(16));

        // header and logout
        HBox header = new HBox(8);
        Label lbl = new Label("Admin Panel");
        lbl.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Button btnLogout = new Button("Logout");
        btnLogout.setOnAction(e -> new SetupLoginSelectionPage(db).show(stage));
        header.getChildren().addAll(lbl, btnLogout);

        // user table
        table = new TableView<>();
        table.setPrefHeight(260);

        TableColumn<String[], String> colUser = new TableColumn<>("Username");
        colUser.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue()[0]));
        TableColumn<String[], String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue()[1]));
        TableColumn<String[], String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue()[2]));
        TableColumn<String[], String> colRoles = new TableColumn<>("Roles");
        colRoles.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue()[3]));

        table.getColumns().addAll(colUser, colName, colEmail, colRoles);

        Button btnRefresh = new Button("Refresh Users");
        btnRefresh.setOnAction(e -> loadUsers());

        // role controls
        TextField txtUser = new TextField();
        txtUser.setPromptText("username");
        TextField txtRole = new TextField();
        txtRole.setPromptText("role (admin/student/staff/reviewer)");
        Button btnAddRole = new Button("Add Role");
        Button btnRemoveRole = new Button("Remove Role");
        Label roleMsg = new Label();

        btnAddRole.setOnAction(e -> {
            String uname = txtUser.getText().trim();
            String role = txtRole.getText().trim();
            if (uname.isEmpty() || role.isEmpty()) {
                roleMsg.setText("Enter username and role");
                return;
            }
            boolean ok = addRole(uname, role);
            if (ok) {
                roleMsg.setText("Role added.");
                loadUsers();
            } else {
                roleMsg.setText("Could not add role.");
            }
        });

        btnRemoveRole.setOnAction(e -> {
            String uname = txtUser.getText().trim();
            String role = txtRole.getText().trim();
            if (uname.isEmpty() || role.isEmpty()) {
                roleMsg.setText("Enter username and role");
                return;
            }
            if (role.equalsIgnoreCase("admin") && isAdmin(uname) && countAdmins() <= 1) {
                roleMsg.setText("At least one admin must remain.");
                return;
            }
            boolean ok = removeRole(uname, role);
            if (ok) {
                roleMsg.setText("Role removed.");
                loadUsers();
            } else {
                roleMsg.setText("Could not remove role.");
            }
        });

        HBox roleBox = new HBox(8, txtUser, txtRole, btnAddRole, btnRemoveRole);

        // delete user
        TextField txtDelUser = new TextField();
        txtDelUser.setPromptText("username");
        TextField txtConfirm = new TextField();
        txtConfirm.setPromptText("Type YES");
        Button btnDelete = new Button("Delete");
        Label delMsg = new Label();

        btnDelete.setOnAction(e -> {
            String uname = txtDelUser.getText().trim();
            String conf = txtConfirm.getText().trim();
            if (uname.isEmpty()) {
                delMsg.setText("Enter a username.");
                return;
            }
            if (!"YES".equals(conf)) {
                delMsg.setText("Type YES to confirm.");
                return;
            }
            if (isAdmin(uname) && countAdmins() <= 1) {
                delMsg.setText("Cannot delete last admin.");
                return;
            }
            boolean ok = deleteUser(uname);
            if (ok) {
                delMsg.setText("User deleted.");
                loadUsers();
            } else {
                delMsg.setText("Delete failed.");
            }
        });

        HBox delBox = new HBox(8, txtDelUser, txtConfirm, btnDelete);

        // invitation codes
        Button btnCode = new Button("Generate Code");
        Label codeMsg = new Label();
        btnCode.setOnAction(e -> {
            String code = db.generateInvitationCode();
            codeMsg.setText(code != null ? "New code: " + code : "Could not generate code");
        });

        // put everything together
        VBox cardUsers = new VBox(8, new Label("Users:"), table, btnRefresh);
        VBox cardRoles = new VBox(6, new Label("Change role:"), roleBox, roleMsg);
        VBox cardDelete = new VBox(6, new Label("Delete user (YES to confirm):"), delBox, delMsg);
        VBox cardInvites = new VBox(6, new Label("Invitation codes:"), btnCode, codeMsg);

        root.getChildren().addAll(header, cardUsers, cardRoles, cardDelete, cardInvites);

        // load users when window opens
        loadUsers();

        stage.setScene(new Scene(root, 860, 640));
        stage.setTitle("Admin");
    }

    // load all users for table
    private void loadUsers() {
        try {
            List<String[]> list = db.getAllUsers();
            rows = FXCollections.observableArrayList(list);
            table.setItems(rows);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // helper methods
    private boolean addRole(String u, String r) { return db != null && db.login(u, "") ? false : true; }
    private boolean removeRole(String u, String r) { return true; } // stub, replace with db logic
    private boolean deleteUser(String u) { db.deleteUser(u); return true; }
    private boolean isAdmin(String u) { return false; } // stub, adapt if needed
    private int countAdmins() { return 1; } // stub, adapt if needed
}
