package com.b00tc4mp.api;

import com.b00tc4mp.data.Data;
import com.b00tc4mp.data.UserData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UsersServlet", urlPatterns = "/hello")
public class UsersServlet extends HttpServlet {

    private static final Gson gson = new Gson();
    private Data data; // Will be initialized in init()

    @Override
    public void init() throws ServletException {
        super.init();
        this.data = Data.get(); // Get singleton instance (with preloaded "pepito")
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        // Read JSON body
        String jsonInput = readRequestBody(request);
        if (jsonInput == null || jsonInput.trim().isEmpty()) {
            sendError(response, out, HttpServletResponse.SC_BAD_REQUEST, "Empty request body");
            return;
        }

        // Parse JSON
        JsonObject json;
        try {
            json = gson.fromJson(jsonInput, JsonObject.class);
        } catch (JsonSyntaxException e) {
            sendError(response, out, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format");
            return;
        }

        // Validate fields
        if (!json.has("name") || !json.has("username") || !json.has("password")) {
            sendError(response, out, HttpServletResponse.SC_BAD_REQUEST,
                    "Missing required fields: name, username, password");
            return;
        }

        String name = json.get("name").getAsString().trim();
        String username = json.get("username").getAsString().trim();
        String password = json.get("password").getAsString();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
            sendError(response, out, HttpServletResponse.SC_BAD_REQUEST,
                    "All fields must be non-empty");
            return;
        }

        // Use Data singleton for user management
        UserData existingUser = data.findUserByUsername(username);
        if (existingUser != null) {
            sendError(response, out, HttpServletResponse.SC_CONFLICT,
                    "Username already taken");
            return;
        }

        // Create and register new user
        UserData newUser = new UserData(name, username, password);
        data.addUser(newUser);

        // Success response
        JsonObject success = new JsonObject();
        success.addProperty("message", "User registered successfully");
        success.addProperty("id", newUser.getId());
        success.addProperty("name", newUser.getName());
        success.addProperty("username", newUser.getUsername());

        response.setStatus(HttpServletResponse.SC_CREATED);
        out.print(gson.toJson(success));
        out.flush();
    }

    // Helper: Read full request body
    private String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = request.getReader()) {
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        return sb.toString();
    }

    // Helper: Send JSON error
    private void sendError(HttpServletResponse response, PrintWriter out, int status, String message) {
        response.setStatus(status);
        JsonObject error = new JsonObject();
        error.addProperty("error", message);
        out.print(gson.toJson(error));
    }
}
