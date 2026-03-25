package org.fooddelivery.ui;

import org.fooddelivery.service.AuthService;
import org.fooddelivery.service.IAuthService;

import javax.swing.*;
import java.awt.*;

public class RegisterScreen extends JPanel {

    private final IAuthService authService;

    private JTextField nameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JLabel messageLabel;

    public RegisterScreen() {
        this.authService = new AuthService();
        setLayout(new GridBagLayout());
        buildUI();
    }

    private void buildUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Create Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Name:"), gbc);
        nameField = new JTextField(20);
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Phone:"), gbc);
        phoneField = new JTextField(20);
        gbc.gridx = 1;
        add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Role:"), gbc);
        roleComboBox = new JComboBox<>(new String[]{"CUSTOMER", "RESTAURANT_OWNER"});
        gbc.gridx = 1;
        add(roleComboBox, gbc);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        add(messageLabel, gbc);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> handleRegister());
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 1;
        add(registerButton, gbc);

        JButton backButton = new JButton("Back to Login");
        backButton.addActionListener(e -> MainFrame.getInstance().showPanel(new LoginScreen()));
        gbc.gridx = 1;
        add(backButton, gbc);
    }

    private void handleRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill in all fields");
            return;
        }

        try {
            authService.register(name, email, phone, password, role);
            messageLabel.setForeground(Color.GREEN);
            messageLabel.setText("Registration successful!");
            Timer timer = new Timer(1500, e -> MainFrame.getInstance().showPanel(new LoginScreen()));
            timer.setRepeats(false);
            timer.start();
        } catch (IllegalArgumentException ex) {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText(ex.getMessage());
        }
    }
}