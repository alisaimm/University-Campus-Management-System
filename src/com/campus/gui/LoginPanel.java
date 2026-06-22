package com.campus.gui;

import com.campus.service.AuthenticationService;
import com.campus.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private AuthenticationService authService;

    private JTextField usernameField;
    private JPasswordField passwordField;

    private JButton loginBtn;

    public LoginPanel(MainFrame mainFrame, AuthenticationService authService) {
        this.mainFrame = mainFrame;
        this.authService = authService;

        this.setBackground(UITheme.SIDEBAR_BG);
        this.setLayout(new GridBagLayout());

        // Modular UI setup
        initializeComponents();
        setupLayout();
        attachListeners();
    }

    /**
     * Initializes the input fields and control components for authentication.
     */
    private void initializeComponents() {
        usernameField = new JTextField(18);
        usernameField.setFont(UITheme.BODY_FONT);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        passwordField = new JPasswordField(18);
        passwordField.setFont(UITheme.BODY_FONT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        loginBtn = new JButton("Secure Sign In");
        loginBtn.setFont(UITheme.HEADER_FONT);
        loginBtn.setBackground(UITheme.ACCENT);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    /**
     * Constructs the login card and centers it on the panel.
     */
    private void setupLayout() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210), 1),
                BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Branding / Title
        JLabel title = new JLabel("University Management System");
        title.setFont(UITheme.TITLE_FONT);
        title.setForeground(UITheme.TEXT_DARK);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        card.add(title, gbc);

        JLabel subtitle = new JLabel("Authorized Access Only");
        subtitle.setFont(UITheme.BODY_FONT);
        subtitle.setForeground(Color.GRAY);
        subtitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        card.add(subtitle, gbc);

        gbc.gridy = 2; card.add(Box.createVerticalStrut(20), gbc);

        // Fields
        gbc.gridwidth = 1; gbc.gridy = 3; gbc.gridx = 0;
        card.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        card.add(usernameField, gbc);

        gbc.gridy = 4; gbc.gridx = 0;
        card.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        card.add(passwordField, gbc);

        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        card.add(Box.createVerticalStrut(15), gbc);

        // Login Button
        gbc.gridy = 6;
        card.add(loginBtn, gbc);

        this.add(card);
    }

    /**
     * Attaches core logic for authentication triggers.
     */
    private void attachListeners() {
        loginBtn.addActionListener(e -> doLogin());
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = authService.login(username, password);
        if (user != null) {
            usernameField.setText("");
            passwordField.setText("");
            mainFrame.showDashboard();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
