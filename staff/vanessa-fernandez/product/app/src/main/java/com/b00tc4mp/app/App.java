package com.b00tc4mp.app;

import javax.swing.*;
import java.awt.*;

// Pequeña aplicación Swing con registro/login y navegación por pantallas
public class App extends JFrame {
    public static void main(String[] args) {
        // Arranca la UI en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> new App().setVisible(true));
    }

    // Gestor para cambiar entre pantallas (Register/Login/Home)
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    // Estado simple en memoria para el usuario registrado
    private String registeredUser = null;
    private String registeredPass = null;

    public App() {
        // Configuración básica de la ventana principal
        setTitle("App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Construye las tres pantallas principales
        JPanel registerPanel = createRegisterPanel();
        JPanel loginPanel = createLoginPanel();
        JPanel homePanel = createHomePanel();

        // Registra las pantallas en el contenedor con sus claves
        cards.add(registerPanel, "Register");
        cards.add(loginPanel, "Login");
        cards.add(homePanel, "Home");

        // Añade el contenedor y muestra inicialmente la pantalla de registro
        add(cards);
        cardLayout.show(cards, "Register");
    }

    private JPanel createRegisterPanel() {
        // Pantalla de registro de usuario
        JPanel panel = new JPanel(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints(); // Objeto de restricciones para GridBagLayout
        gbc.insets = new Insets(5, 5, 5, 5); // Márgenes externos (arriba, izquierda, abajo, derecha) de 5 px
        gbc.fill = GridBagConstraints.HORIZONTAL; // El componente se estira horizontalmente dentro de su celda

        JLabel title = new JLabel("Register", SwingConstants.CENTER); // Etiqueta de título centrada
        title.setBackground(Color.CYAN); // Establece color de fondo cian
        title.setOpaque(true); // Necesario para que se pinte el color de fondo en un JLabel
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f)); // Fuente en negrita, tamaño 18
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; // Posición (col 0, fila 0) ocupando 2 columnas
        panel.add(title, gbc); // Añade el título al panel usando las restricciones

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(registerButton, gbc);

        JButton toLoginButton = new JButton("Go to Login");
        gbc.gridy = 4;
        panel.add(toLoginButton, gbc);

        JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
        gbc.gridy = 5;
        panel.add(messageLabel, gbc);

        // Al registrarse, valida campos y guarda usuario/contraseña en memoria
        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please fill in all fields.");
            } else {
                registeredUser = username;
                registeredPass = password;

                messageLabel.setText("Registration successful!. Go to Login.");
            }
        });

        // Navega a la pantalla de Login y limpia los campos
        toLoginButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            messageLabel.setText("");

            cardLayout.show(cards, "Login");
        });

        return panel;
    }

    private JPanel createLoginPanel() {
        // Pantalla de inicio de sesión
        JPanel panel = new JPanel(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        JTextField usernameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        JPasswordField passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        JButton toRegisterButton = new JButton("Go to Register");
        gbc.gridy = 4;
        panel.add(toRegisterButton, gbc);

        JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
        gbc.gridy = 5;
        panel.add(messageLabel, gbc);

        // Intenta validar las credenciales contra las registradas en memoria
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.equals(registeredUser) && password.equals(registeredPass)) {
                messageLabel.setText("");

                // TODO call https://zenquotes.io/api/today and display the quote in Home panel
                // Al autenticarse correctamente, se muestra la pantalla Home
                cardLayout.show(cards, "Home");
            } else {
                messageLabel.setText("Invalid credentials. Try again.");
            
            } 
        });

        // Navega a la pantalla de Registro y limpia los campos
        toRegisterButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            messageLabel.setText("");

            cardLayout.show(cards, "Register");
        });

        return panel;
    }

    private JPanel createHomePanel() {
        // Pantalla Home mostrada tras iniciar sesión
        JPanel panel = new JPanel(new BorderLayout());
        
       JLabel welcome = new JLabel("Welcome to the Home Page!", SwingConstants.CENTER);
       welcome.setFont(welcome.getFont().deriveFont(Font.BOLD, 20f));
       panel.add(welcome, BorderLayout.CENTER);

       JButton logoutButton = new JButton("Logout");
       panel.add(logoutButton, BorderLayout.SOUTH);

       // Vuelve a la pantalla de Login al cerrar sesión
       logoutButton.addActionListener(e -> {
           cardLayout.show(cards, "Login");
       });

         return panel;
    }
}
