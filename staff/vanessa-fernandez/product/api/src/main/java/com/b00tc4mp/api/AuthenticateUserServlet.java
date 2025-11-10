package com.b00tc4mp.api;

import com.b00tc4mp.logic.Logic;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import com.b00tc4mp.api.helper.HandlerHelper;
import com.b00tc4mp.api.helper.JwtHelper;

@WebServlet(name = "AuthenticateUserServlet", urlPatterns = "/users/auth")
public class AuthenticateUserServlet extends HttpServlet {

    private static final Gson gson = new Gson();
    
    private Logic logic;

    @Override
    public void init() throws ServletException {
        super.init();
    
        this.logic = Logic.get();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();

        // Read JSON body
        String jsonInput = HandlerHelper.readRequestBody(request);
        if (jsonInput == null || jsonInput.trim().isEmpty()) {
            HandlerHelper.sendError(response, out, HttpServletResponse.SC_BAD_REQUEST, Exception.class.getSimpleName(), "Empty request body");
            return;
        }

        JsonObject json;
        String username;
        String password;

        try {
            json = gson.fromJson(jsonInput, JsonObject.class);

            username = json.get("username").getAsString().trim();
            password = json.get("password").getAsString();
        } catch (JsonSyntaxException e) {
            HandlerHelper.sendError(response, out, HttpServletResponse.SC_BAD_REQUEST, e.getClass().getSimpleName(), "Invalid JSON format");
            return;
        } catch (NullPointerException e) {
            HandlerHelper.sendError(response, out, HttpServletResponse.SC_BAD_REQUEST, e.getClass().getSimpleName(), "Missing fields in JSON");
            return;
        }

        try {
            String userId = logic.authenticateUser(username, password);

            String token = JwtHelper.issueToken(userId);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            out.print("\"" + token + "\"");
            out.flush();
        } catch (Exception e) {
            HandlerHelper.sendError(response, out, HttpServletResponse.SC_BAD_REQUEST, e.getClass().getSimpleName(), e.getMessage());
        }
    }

   
}