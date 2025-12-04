package com.b00tc4mp.logic;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import com.b00tc4mp.data.Data;
import com.b00tc4mp.data.DataImpl;
import com.b00tc4mp.data.UserData;
import com.b00tc4mp.error.CredentialException;
import com.b00tc4mp.error.DuplicityException;
import com.b00tc4mp.error.NotFoundException;
import com.b00tc4mp.error.SystemException;
import com.b00tc4mp.error.ValidationException;
import com.b00tc4mp.validation.Validate;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Logic {

    private static Logic instance;

    private Data data;

    private final Gson gson;

    private Logic() {
        data = DataImpl.get();
        gson = new Gson();
    }

    public static Logic get() {
        if (instance == null) {
            instance = new Logic();
        }
        return instance;
    }

    public void registerUser(String name, String username, String password, String passwordRepeat) throws Exception {
        Validate.name(name);
        Validate.username(username);
        Validate.passwords(password, passwordRepeat);

        UserData user = data.findUserByUsername(username);

        if (user != null) {
            throw new DuplicityException("user already exists");
        }

        data.addUser(new UserData(UUID.randomUUID().toString(), name, username, password));
    }

    public String authenticateUser(String username, String password) throws ValidationException, NotFoundException, CredentialException {
        Validate.username(username);
        Validate.password(password);

        UserData user = data.findUserByUsername(username);

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        if (!user.getPassword().equals(password)) {
            throw new CredentialException("Invalid password");
        }

        return user.getId();
    }

    public User getUserInfo(String userId) throws Exception {
        Validate.userId(userId);

        UserData user = data.findUserById(userId);

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        return new User(user.getName(), user.getUsername());
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
            throw new SystemException("Failed to fetch quote: " + e);
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
