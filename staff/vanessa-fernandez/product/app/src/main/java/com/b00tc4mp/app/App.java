package com.b00tc4mp.app;

import javax.swing.*;

import com.b00tc4mp.logic.Logic;
import com.b00tc4mp.logic.ZenQuote;

import java.awt.*;

public class App extends JFrame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App().setVisible(true));
    }

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    private JLabel welcome;
    private JTextPane quoteArea;

    private Logic logic;

    public App() {
        setTitle("App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        logic = Logic.get();

        // Panels
        JPanel registerPanel = createRegisterPanel();
        JPanel loginPanel = createLoginPanel();
        JPanel homePanel = createHomePanel();

        cards.add(registerPanel, "register");
        cards.add(loginPanel, "login");
        cards.add(homePanel, "home");

        add(cards);
        cardLayout.show(cards, "login");
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Register", SwingConstants.CENTER);
        title.setOpaque(true);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Name:"), gbc);
        JTextField nameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Username:"), gbc);
        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Password:"), gbc);
        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Confirm Password:"), gbc);
        JPasswordField confirmPasswordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(confirmPasswordField, gbc);

        JButton registerBtn = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(registerBtn, gbc);

        JButton toLoginBtn = new JButton("Go to Login");
        gbc.gridy = 6;
        panel.add(toLoginBtn, gbc);

        JLabel message = new JLabel("", SwingConstants.CENTER);
        gbc.gridy = 7;
        panel.add(message, gbc);

        registerBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPass = new String(confirmPasswordField.getPassword());

            try {
                logic.registerUser(name, username, password, confirmPass);

                nameField.setText("");
                usernameField.setText("");
                passwordField.setText("");
                confirmPasswordField.setText("");
                message.setText("");

                cardLayout.show(cards, "login");
            } catch (Exception ex) {
                message.setText("Error: " + ex.getMessage());
            }
        });

        toLoginBtn.addActionListener(e -> {
            nameField.setText("");
            usernameField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
            message.setText("");

            cardLayout.show(cards, "login");
        });

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        JButton loginBtn = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(loginBtn, gbc);

        JButton toRegisterBtn = new JButton("Go to Register");
        gbc.gridy = 4;
        panel.add(toRegisterBtn, gbc);

        JLabel message = new JLabel("", SwingConstants.CENTER);
        gbc.gridy = 5;
        panel.add(message, gbc);

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            try {
                logic.loginUser(username, password);

                usernameField.setText("");
                passwordField.setText("");
                message.setText("");

                String name = logic.getCurrentUser().getName();

                ZenQuote quote = logic.getZenQuoteOfDay();

                welcome.setText("Welcome, " + name + "!");

                quoteArea.setText("<html><center>&quot;" + quote.getQuote() + "&quot;<br>- " + quote.getAuthor()
                        + "</center></html>");

                cardLayout.show(cards, "home");
            } catch (Exception ex) {
                message.setText("Error: " + ex.getMessage());
            }
        });

        toRegisterBtn.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            message.setText("");

            cardLayout.show(cards, "register");
        });

        return panel;
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        welcome = new JLabel("Welcome Home!", SwingConstants.CENTER);
        welcome.setFont(welcome.getFont().deriveFont(Font.BOLD, 20f));
        panel.add(welcome, BorderLayout.NORTH);

        quoteArea = new JTextPane();
        quoteArea.setEditable(false);
        quoteArea.setContentType("text/html");
        quoteArea.setFont(quoteArea.getFont().deriveFont(Font.ITALIC, 14f));

        panel.add(quoteArea, BorderLayout.CENTER);

        JButton logoutBtn = new JButton("Logout");
        panel.add(logoutBtn, BorderLayout.SOUTH);
        logoutBtn.addActionListener(e -> cardLayout.show(cards, "login"));

        return panel;
    }
}