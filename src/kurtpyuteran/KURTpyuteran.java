/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package kurtpyuteran;

/**
 *
 * @author User
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class KURTpyuteran {
    // Database connection variables
    private static final String DB_URL = "jdbc:mysql://localhost:3306/KURTpyuteran";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KURTpyuteran().showLoginScreen());
    }

    public void showLoginScreen() {
        JFrame loginFrame = new JFrame("Admin Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 300);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(loginButton);
        panel.add(signUpButton);

        loginFrame.add(panel);
        loginFrame.setVisible(true);

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (authenticateAdmin(username, password)) {
                JOptionPane.showMessageDialog(loginFrame, "Login successful!");
                loginFrame.dispose();
                createAndShowGUI();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        signUpButton.addActionListener(e -> showSignUpScreen());
    }

    private boolean authenticateAdmin(String username, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {

            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error during authentication: " + e.getMessage());
            return false;
        }
    }

    public void showSignUpScreen() {
        JFrame signUpFrame = new JFrame("Admin Sign Up");
        signUpFrame.setSize(400, 300);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();
        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPassField = new JPasswordField();

        JButton registerButton = new JButton("Register");

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(confirmPassLabel);
        panel.add(confirmPassField);
        panel.add(new JLabel());
        panel.add(registerButton);

        signUpFrame.add(panel);
        signUpFrame.setVisible(true);

        registerButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());
            String confirmPassword = new String(confirmPassField.getPassword());

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(signUpFrame, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {

                statement.setString(1, username);
                statement.setString(2, password);
                statement.executeUpdate();

                JOptionPane.showMessageDialog(signUpFrame, "Admin registered successfully!");
                signUpFrame.dispose();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(signUpFrame, "Error during registration: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Service Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Main Panel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Table to display data
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons for actions
        JPanel buttonPanel = new JPanel();
        JButton loadButton = new JButton("Load Users");
        JButton addButton = new JButton("Add User");
        JButton updateButton = new JButton("Update User");
        JButton deleteButton = new JButton("Delete User");

        buttonPanel.add(loadButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Load Users Button Action
        loadButton.addActionListener(e -> loadUsers(table));

        // Add User Button Action
        addButton.addActionListener(e -> addUser());

        frame.add(panel);
        frame.setVisible(true);
    }

    private void loadUsers(JTable table) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM users")) {

            // Get metadata to set table model
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Column names
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            // Data rows
            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst();

            String[][] data = new String[rowCount][columnCount];
            int row = 0;
            while (resultSet.next()) {
                for (int col = 1; col <= columnCount; col++) {
                    data[row][col - 1] = resultSet.getString(col);
                }
                row++;
            }

            // Set table model
            table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error loading users: " + e.getMessage());
        }
    }

    private void addUser() {
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField contactNumberField = new JTextField();

        Object[] message = {
            "First Name:", firstNameField,
            "Last Name:", lastNameField,
            "Username:", usernameField,
            "Password:", passwordField,
            "Email:", emailField,
            "Contact Number:", contactNumberField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Add User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(
                         "INSERT INTO users (first_name, last_name, username, password, email, contact_number) VALUES (?, ?, ?, ?, ?, ?, 'user')")) {

                preparedStatement.setString(1, firstNameField.getText());
                preparedStatement.setString(2, lastNameField.getText());
                preparedStatement.setString(3, usernameField.getText());
                preparedStatement.setString(4, passwordField.getText());
                preparedStatement.setString(5, emailField.getText());
                preparedStatement.setString(6, contactNumberField.getText());

                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(null, "User added successfully!");

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error adding user: " + e.getMessage());
            }
        }
    }
}
