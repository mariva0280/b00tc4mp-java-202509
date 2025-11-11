package com.b00tc4mp.logic;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.http.HttpRequest.BodyPublishers;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import com.b00tc4mp.data.Data;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Logic {

    private static Logic instance;

    protected String userId;

    private Data data;

    private final Gson gson;

    private Logic() {
        data = Data.get();

        gson = new Gson();
    }

    public static Logic get() {
        if (instance == null) {
            instance = new Logic();
        }

        return instance;
    }

    public void registerUser(String name, String username, String password, String confirmPassword) throws Exception {
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

        try {
            String jsonBody = String.format("""
            {
                "name": "%s",
                "username": "%s",
                "password": "%s",
                "confirmPassword": "%s"
            }
            """, name, username, password, confirmPassword);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/users"))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                return;
            }

            Gson gson = new Gson();
            JsonObject errorResponse = gson.fromJson(response.body(), JsonObject.class);

            String error = errorResponse.get("error").getAsString();
            String message = errorResponse.get("message").getAsString();

            throw new Exception(error + ": " + message);
        } catch (Exception e) {
            throw new Exception("error in register: " + e.getMessage());
        }
    }

    public void loginUser(String username, String password) throws Exception {
        if (username == null || username.isEmpty()) {
            throw new Exception("Username cannot be empty");
        }

        if (password == null || password.isEmpty()) {
            throw new Exception("Password cannot be empty");
        }

        try {
            String jsonBody = String.format("""
            {
                "username": "%s",
                "password": "%s"
            }
            """, username, password);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/users/auth"))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                JsonPrimitive loginResponse = gson.fromJson(response.body(), JsonPrimitive.class);

                String token = loginResponse.getAsString();

                data.setToken(token);

                System.out.println(token);

                return;
            }

            Gson gson = new Gson();
            JsonObject errorResponse = gson.fromJson(response.body(), JsonObject.class);

            String error = errorResponse.get("error").getAsString();
            String message = errorResponse.get("message").getAsString();

            throw new Exception(error + ": " + message);
        } catch (Exception e) {
            throw new Exception("error in login: " + e.getMessage());
        }
    }

    public void logoutUser() {
        data.setToken(null);
    }

    public boolean isUserLoggedIn() {
        return data.getToken() != null;
    }

    public User getUserInfo() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/users/info"))
                    .header("Authorization", "Bearer " + data.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println(response.statusCode());
            System.out.println(response.body());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                JsonObject userInfoResponse = gson.fromJson(response.body(), JsonObject.class);

                String name = userInfoResponse.get("name").getAsString();
                String username = userInfoResponse.get("username").getAsString();

                User user = new User(name, username);

                return user;
            }

            Gson gson = new Gson();
            JsonObject errorResponse = gson.fromJson(response.body(), JsonObject.class);

            String error = errorResponse.get("error").getAsString();
            String message = errorResponse.get("message").getAsString();

            throw new Exception(error + ": " + message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("error in get user info: " + e.getMessage());
        }
    }

    public ZenQuote getZenQuoteOfDay() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://zenquotes.io/api/today"))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();

            if (status != 200) {
                throw new Exception("Failed to fetch quote, status code: " + status);
            }

            // Parse JSON using Gson
            QuoteResponse[] quotes = gson.fromJson(response.body(), QuoteResponse[].class);

            if (quotes.length == 0) {
                throw new Exception("No quote found in response");
            }

            QuoteResponse quoteObj = quotes[0];
            return new ZenQuote(quoteObj.quote, quoteObj.author);

        } catch (Exception e) {
            throw new Exception("Failed to fetch quote: " + e.getMessage());
        }
    }

    // Inner class to map the JSON structure from zenquotes.io
    private static class QuoteResponse {

        @SerializedName("q")
        String quote;

        @SerializedName("a")
        String author;
    }
}