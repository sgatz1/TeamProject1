// The db we are using ensure it is cross-platform available and does not need excessive downloads to work, it locally loads everything and we can simply clean it for testing.
// User Table is attached to a live db view for admins

package databasePart1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// this class is for handling the database
public class DatabaseHelper {
    private Connection conn;

    public DatabaseHelper() {
        try {
            // load h2 database driver
            Class.forName("org.h2.Driver");
            // connect to local database file
            conn = DriverManager.getConnection("jdbc:h2:./cse360db", "sa", "Password");
            // make tables if they dont exist
            initializeTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // creates the tables if missing
    private void initializeTables() throws SQLException {
        Statement stmt = conn.createStatement();

        stmt.execute("CREATE TABLE IF NOT EXISTS USERS (" +
                "ID INT AUTO_INCREMENT PRIMARY KEY," +
                "USERNAME VARCHAR(255) UNIQUE," +
                "PASSWORD VARCHAR(255)," +
                "EMAIL VARCHAR(255))");

        stmt.execute("CREATE TABLE IF NOT EXISTS ROLES (" +
                "USERNAME VARCHAR(255)," +
                "ROLE VARCHAR(255))");

        stmt.execute("CREATE TABLE IF NOT EXISTS INVITATIONS (" +
                "CODE VARCHAR(255) PRIMARY KEY," +
                "USED BOOLEAN DEFAULT FALSE)");

        stmt.close();
    }

    // add a new user
    public boolean register(String username, String password, String email) {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL) VALUES (?, ?, ?)")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // check login
    public boolean login(String username, String password) {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM USERS WHERE USERNAME = ? AND PASSWORD = ?")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            boolean ok = rs.next();
            rs.close();
            return ok;
        } catch (SQLException e) {
            return false;
        }
    }

    // give a user a role
    public void addRole(String username, String role) {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO ROLES (USERNAME, ROLE) VALUES (?, ?)")) {
            ps.setString(1, username);
            ps.setString(2, role);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // get roles for a user
    public List<String> getUserRoles(String username) {
        List<String> roles = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT ROLE FROM ROLES WHERE USERNAME = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                roles.add(rs.getString("ROLE"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    // remove a user and their roles
    public void deleteUser(String username) {
        try {
            try (PreparedStatement ps1 = conn.prepareStatement("DELETE FROM ROLES WHERE USERNAME = ?")) {
                ps1.setString(1, username);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = conn.prepareStatement("DELETE FROM USERS WHERE USERNAME = ?")) {
                ps2.setString(1, username);
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    ///  fixed this users table to ensure it successfully displays all users on db table for admin

    public List<String[]> getAllUsers() {
        List<String[]> users = new ArrayList<>();
        try {
            PreparedStatement psUsers = conn.prepareStatement("SELECT USERNAME, EMAIL FROM USERS");
            ResultSet rsUsers = psUsers.executeQuery();

            while (rsUsers.next()) {
                String uname = rsUsers.getString("USERNAME");
                String email = rsUsers.getString("EMAIL");

                // get roles for this user
                PreparedStatement psRoles = conn.prepareStatement("SELECT ROLE FROM ROLES WHERE USERNAME = ?");
                psRoles.setString(1, uname);
                ResultSet rsRoles = psRoles.executeQuery();

                List<String> roleList = new ArrayList<>();
                while (rsRoles.next()) {
                    roleList.add(rsRoles.getString("ROLE"));
                }
                rsRoles.close();
                psRoles.close();

                String roles = roleList.isEmpty() ? "(none)" : String.join(",", roleList);

                // derive name from email (before the @)
                String name;
                if (email != null && email.contains("@")) {
                    name = email.substring(0, email.indexOf("@"));
                } else {
                    name = "(not set)";
                }

                users.add(new String[]{uname, name, email == null ? "" : email, roles});
            }

            rsUsers.close();
            psUsers.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // make a random invite code
    public String generateInvitationCode() {
        String code = UUID.randomUUID().toString().substring(0, 8);
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO INVITATIONS (CODE, USED) VALUES (?, FALSE)")) {
            ps.setString(1, code);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return code;
    }

    // check if an invite code is valid
    public boolean validateInvitationCode(String code) {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM INVITATIONS WHERE CODE = ? AND USED = FALSE")) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();
            boolean valid = rs.next();
            rs.close();
            if (valid) {
                try (PreparedStatement upd = conn.prepareStatement(
                        "UPDATE INVITATIONS SET USED = TRUE WHERE CODE = ?")) {
                    upd.setString(1, code);
                    upd.executeUpdate();
                }
            }
            return valid;
        } catch (SQLException e) {
            return false;
        }
    }

    // change a password
    public boolean resetPassword(String username, String newPass) {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE USERS SET PASSWORD = ? WHERE USERNAME = ?")) {
            ps.setString(1, newPass);
            ps.setString(2, username);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    // get user email (used in UserHomePage)
    public String getUserEmail(String username) {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT EMAIL FROM USERS WHERE USERNAME = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            String email = null;
            if (rs.next()) {
                email = rs.getString("EMAIL");
            }
            rs.close();
            return email;
        } catch (SQLException e) {
            return null;
        }
    }

    // update user email
    public boolean updateUserEmail(String username, String newEmail) {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE USERS SET EMAIL = ? WHERE USERNAME = ?")) {
            ps.setString(1, newEmail);
            ps.setString(2, username);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    // check if db has no users
    public boolean isDatabaseEmpty() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS CNT FROM USERS")) {
            return rs.next() && rs.getInt("CNT") == 0;
        } catch (SQLException e) {
            return true;
        }
    }
}
//end 
