package com.b00tc4mp.logic;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest.BodyPublishers;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import com.b00tc4mp.data.Data;
import com.b00tc4mp.error.CredentialException;
import com.b00tc4mp.error.DuplicityException;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import com.b00tc4mp.error.ExceptionProvider;
import com.b00tc4mp.error.NotFoundException;
import com.b00tc4mp.error.SystemException;
import com.b00tc4mp.error.ValidationException;
import com.b00tc4mp.logic.helper.Config;
import com.b00tc4mp.validation.Validate;

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

    public void registerUser(String name, String username, String password, String passwordRepeat) throws ValidationException, DuplicityException {
        Validate.name(name);
        Validate.username(username);
        Validate.passwords(password, passwordRepeat);

        try {
            String jsonBody = String.format("""
            {
                "name": "%s",
                "username": "%s",
                "password": "%s",
                "passwordRepeat": "%s"
            }
            """, name, username, password, passwordRepeat);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Config.getApiUrl() + "/users"))
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

            throw ExceptionProvider.newInstance(error, message);
        } catch (IOException e) {
            throw new SystemException("connection error", e);
        } catch (InterruptedException e) {
            throw new SystemException("connection error", e);
        } catch (URISyntaxException e) {
            throw new SystemException("connection error", e);
        }
    }

    public void loginUser(String username, String password) throws ValidationException, NotFoundException, CredentialException {
        Validate.username(username);
        Validate.password(password);

        try {
            String jsonBody = String.format("""
            {
                "username": "%s",
                "password": "%s"
            }
            """, username, password);

            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(Config.getApiUrl() + "/users/auth"))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Gson gson = new Gson();
                JsonPrimitive loginResponse = gson.fromJson(response.body(), JsonPrimitive.class);

                String token = loginResponse.getAsString();

                data.setToken(token);

                return;
            }

            Gson gson = new Gson();
            JsonObject errorResponse = gson.fromJson(response.body(), JsonObject.class);

            String error = errorResponse.get("error").getAsString();
            String message = errorResponse.get("message").getAsString();

            throw ExceptionProvider.newInstance(error, message);
        } catch (IOException e) {
            throw new SystemException("connection error", e);
        } catch (InterruptedException e) {
            throw new SystemException("connection error", e);
        } catch (URISyntaxException e) {
            throw new SystemException("connection error", e);
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
                    .uri(new URI(Config.getApiUrl() + "/users/info"))
                    .header("Authorization", "Bearer " + data.getToken())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

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

            throw ExceptionProvider.newInstance(error, message);
        } catch (IOException e) {
            throw new SystemException("connection error", e);
        } catch (InterruptedException e) {
            throw new SystemException("connection error", e);
        } catch (URISyntaxException e) {
            throw new SystemException("connection error", e);
        }
    }

    public ZenQuote getZenQuoteOfDay() throws Exception {
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
    }

    // Inner class to map the JSON structure from zenquotes.io
    private static class QuoteResponse {

        @SerializedName("q")
        String quote;

        @SerializedName("a")
        String author;
    }
}
