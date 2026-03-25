package org.fooddelivery.ui;

import org.fooddelivery.model.User;
import org.fooddelivery.service.AuthService;
import org.fooddelivery.service.IAuthService;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class LoginScreen extends JPanel {

    private final IAuthService authService;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public LoginScreen() {
        this.authService = new AuthService();
        setLayout(new GridBagLayout());
        buildUI();
    }

    private void buildUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Food Delivery App", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(passwordField, gbc);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(messageLabel, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1;
        add(loginButton, gbc);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> MainFrame.getInstance().showPanel(new RegisterScreen()));
        gbc.gridx = 1;
        add(registerButton, gbc);
    }

    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill in all fields");
            return;
        }

        try {
            Optional<User> user = authService.login(email, password);
            if (user.isPresent()) {
                if (user.get().getRole().equals("CUSTOMER")) {
                    MainFrame.getInstance().showPanel(new CustomerDashboard(user.get()));
                } else {
                    MainFrame.getInstance().showPanel(new RestaurantDashboard(user.get()));
                }
            } else {
                messageLabel.setText("Invalid email or password");
            }
        } catch (IllegalArgumentException ex) {
            messageLabel.setText(ex.getMessage());
        }
    }
}