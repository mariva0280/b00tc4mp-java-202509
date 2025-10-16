package com.b00tc4mp.app.logic;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import org.json.JSONArray;
import org.json.JSONObject;

import com.b00tc4mp.app.data.Data;
import com.b00tc4mp.app.data.UserData;


public class Logic {
    private static Logic instance;

    protected String userId;

    private Data data;

    private Logic() {
        data = Data.get();
    }

    public static Logic get() {
        if (instance == null) {
            instance = new Logic();
        }
        return instance;
    }

    public void registerUser(String name, String username, String password, String confirmPassword) throws Exception {
        // Implementa la lógica para registrar un nuevo usuario
        if (name == null || name.isEmpty()) {
            throw new Exception("Name cannot be empty");
        }

        if (username == null || username.isEmpty()) {
            throw new Exception("Username cannot be empty");
        }

        if (password == null || password.isEmpty()) {
            throw new Exception("Password cannot be empty");
        }

        if (confirmPassword == null || confirmPassword.isEmpty()) {
            throw new Exception("Confirm Password cannot be empty");
        }

        if (!password.equals(confirmPassword)) {
            throw new Exception("Passwords do not match");
        }

        UserData user = data.findUserByUsername(username);

        if (user != null) {
            throw new Exception("Username already exists");
        }

        data.addUser(new UserData(name, username, password));
    }

    public void loginUser(String username, String password) throws Exception {
        // Implementa la lógica para autenticar a un usuario
        if (username == null || username.isEmpty()) {
            throw new Exception("Username cannot be empty");
        }

        if (password == null || password.isEmpty()) {
            throw new Exception("Password cannot be empty");
        }

        UserData user = data.findUserByUsername(username);
        
        if (user == null) {
            throw new Exception("User not found");
        }

        if (!user.getPassword().equals(password)) {
            throw new Exception("Invalid password");
        }

        this.userId = user.getId();
    }

    public void logoutUser() {
        // Implementa la lógica para cerrar la sesión del usuario
        this.userId = null;
    }

    public boolean isUserLoggedIn() {
        // Implementa la lógica para verificar si un usuario está autenticado
        return this.userId != null;
    }

    public User getCurrentUser() throws Exception {
        // Implementa la lógica para obtener la información del usuario autenticado
        if (this.userId == null) {
            throw new Exception("No user is logged in");
        }

        UserData user = data.findUserById(this.userId);

        return new User(user.getId(), user.getName(), user.getUsername());
    }   

    public ZenQuote getZenQuoteOfDay() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://zenquotes.io/api/today"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONArray jsonArray = new JSONArray(response.body());
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            String quote = jsonObject.getString("q");
            String author = jsonObject.getString("a");

            return new ZenQuote(quote, author);

        } catch (Exception e) {
            throw new Exception("Failed to fetch Zen Quote of the Day: " + e.getMessage());
        }
    }

}
