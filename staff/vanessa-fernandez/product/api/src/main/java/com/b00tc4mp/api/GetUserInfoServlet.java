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
import com.b00tc4mp.logic.User;

@WebServlet(name = "GetUserInfoServlet", urlPatterns = "/users/info")
public class GetUserInfoServlet extends HttpServlet {

    private static final Gson gson = new Gson();

    private Logic logic;

    @Override
    public void init() throws ServletException {
        super.init();

        this.logic = Logic.get();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();

        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new Exception("Missing or invalid Authorization header");
            }

            String token = authHeader.substring("Bearer ".length()).trim();

            String userId = JwtHelper.validateToken(token);

            User user = logic.getUserInfo(userId);

            String json = gson.toJson(user);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");

            out.write(json);
            out.flush();
        } catch (Exception e) {
            HandlerHelper.sendError(response, out, HttpServletResponse.SC_BAD_REQUEST, e.getClass().getSimpleName(), e.getMessage());
        }
    }

}
