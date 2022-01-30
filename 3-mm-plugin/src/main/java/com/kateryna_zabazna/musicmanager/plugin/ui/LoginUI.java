package com.kateryna_zabazna.musicmanager.plugin.ui;

import com.kateryna_zabazna.musicmanager.application.exception.UserAlreadyExistsException;
import com.kateryna_zabazna.musicmanager.application.exception.UserNotFoundException;
import com.kateryna_zabazna.musicmanager.application.exception.WrongPasswordException;
import com.kateryna_zabazna.musicmanager.application.observer.LoginObserver;
import com.kateryna_zabazna.musicmanager.application.service.impl.UserServiceImpl;
import com.kateryna_zabazna.musicmanager.domain.user.User;
import com.kateryna_zabazna.musicmanager.domain.user.UserRepository;
import com.kateryna_zabazna.musicmanager.plugin.repository.UserRepositoryImpl;

import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {

    // UI Components
    private JLabel errorLabel;
    private final JTextField textFieldUsername = new JTextField();
    private final JPasswordField textFieldPassword = new JPasswordField();

    // Members
    private final UserServiceImpl userService;
    private final LoginObserver loginObserver;

    public LoginUI(LoginObserver loginObserver) {
        this.loginObserver = loginObserver;

        // Create user service
        UserRepository userRepository = new UserRepositoryImpl();
        this.userService = new UserServiceImpl(userRepository);

        // Setup window
        setupUI();
    }

    private void setupUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(0, 0, 350, 150);
        setTitle("Music Manager - Login");
        setResizable(false);
        setLocationRelativeTo(null);

        // Set root layout
        JPanel rootPanel = new JPanel(new GridBagLayout());
        rootPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(rootPanel);

        // Info/error label
        JLabel labelInfo = new JLabel("Please enter your login credentials below");
        UIHelper.placeUIComp(rootPanel, labelInfo, 0, 0, 2, 1, 2.0, 1.0, GridBagConstraints.HORIZONTAL);

        // Username text field
        JLabel labelUsername = new JLabel("Username:");
        UIHelper.placeUIComp(rootPanel, labelUsername, 0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.HORIZONTAL);
        UIHelper.placeUIComp(rootPanel, textFieldUsername, 1, 1, 1, 1, 1.0, 1.0, GridBagConstraints.HORIZONTAL);

        // Password text field
        JLabel labelPassword = new JLabel("Password:");
        UIHelper.placeUIComp(rootPanel, labelPassword, 0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.HORIZONTAL);
        textFieldPassword.addActionListener(e -> attemptToLogin());
        UIHelper.placeUIComp(rootPanel, textFieldPassword, 1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.HORIZONTAL);

        // Error label
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        UIHelper.placeUIComp(rootPanel, errorLabel, 0, 3, 2, 1, 2.0, 1.0, GridBagConstraints.HORIZONTAL);

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> register());
        UIHelper.placeUIComp(rootPanel, registerButton, 0, 4, 1, 1, 1.0, 1.0, GridBagConstraints.HORIZONTAL);

        // Login button
        JButton buttonLogin = new JButton("LogIn");
        buttonLogin.addActionListener(e -> attemptToLogin());
        UIHelper.placeUIComp(rootPanel, buttonLogin, 1, 4, 1, 1, 1.0, 1.0, GridBagConstraints.HORIZONTAL);

        // Set focus to username field
        textFieldUsername.requestFocus();
    }

    private void attemptToLogin() {
        errorLabel.setText(null);
        String username = textFieldUsername.getText().trim();
        String password = new String(textFieldPassword.getPassword());
        if (!validateFields(username, password)) {
            return;
        }

        // Try to log in
        try {
            User user = userService.login(username, password);
            dispose();
            loginObserver.onLogin(user);
        } catch (UserNotFoundException | WrongPasswordException ex) {
            errorLabel.setText(ex.getMessage());
        }
    }

    private void register() {
        errorLabel.setText(null);
        String username = textFieldUsername.getText().trim();
        String password = new String(textFieldPassword.getPassword());
        if (!validateFields(username, password)){
            return;
        }

        // Try to register
        try {
            User user = userService.register(username, password);
            dispose();
            loginObserver.onLogin(user);
        } catch (UserAlreadyExistsException ex) {
            errorLabel.setText(ex.getMessage());
        }
    }

    private boolean validateFields(String username, String password) {
        if (username == null || username.isEmpty()) {
            errorLabel.setText("Username cannot be empty");
            return false;
        }
        if (password == null || password.isEmpty()) {
            errorLabel.setText("Password cannot be empty");
            return false;
        }
        return true;
    }
}
