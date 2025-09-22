package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.sql.SQLException;

import databasePart1.*;

/**
 * The SetupAdmin class handles the setup process for creating an administrator account.
 * This is intended to be used by the first user to initialize the system with admin credentials.
 */
public class AdminSetupPage {
	
    private final DatabaseHelper databaseHelper;

    public AdminSetupPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
    	// Input fields for userName and password
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Admin userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
      
        
        

        Button setupButton = new Button("Setup");
        
        setupButton.setOnAction(a -> {
        	// Retrieve user input
        	boolean uservalid = false;
        	boolean passvalid = false;
            String userName = userNameField.getText();
            String password = passwordField.getText();
            
            String usernamevalid = UserNameRecognizer.checkForValidUserName(userName);
            System.out.println(usernamevalid);
            
            //Checks if the username is valid, once a valid username is submitted, uservalid is set to true
       
            if(usernamevalid.contains("*** ERROR ***"))
            	
            {
            	System.out.println("Username is Invalid");
                
            }
            else
            {
            	System.out.println("Username is Valid");
            	uservalid = true;
            }
        
          //Checks if the password is valid, once a valid password is submitted, password is set to true
            String passwordvalid = PasswordEvaluator.evaluatePassword(password);
            System.out.println(passwordvalid);
            if(passwordvalid.contains("conditions were not satisfied") || passwordvalid.contains("*** Error *** An invalid character has been found!"))
            {
            	System.out.println("Password is Invalid");
            }
            
            else
            {
            	System.out.println("Password is Valid");
            	passvalid = true;
            }
            
            
            
            
            
           
            
            	
         
            
            
            if(uservalid && passvalid)
            	try {
            	// Create a new User object with admin role and register in the database
            	User user=new User(userName, password, "admin");
                databaseHelper.register(user);
                System.out.println("Administrator setup completed.");
                
                // Navigate to the Welcome Login Page
                new WelcomeLoginPage(databaseHelper).show(primaryStage,user);
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        VBox layout = new VBox(10, userNameField, passwordField, setupButton);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Administrator Setup");
        primaryStage.show();
    }
}
