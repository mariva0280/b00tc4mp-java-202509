package com.b00tc4mp.api.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class HandlerHelper {

    private static final Gson gson = new Gson();

     public static String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = request.getReader()) {
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        return sb.toString();
    }


    public static void sendError(HttpServletResponse response, PrintWriter out, int status, String error, String message) {
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("error", error);
        jsonObject.addProperty("message", message);

        out.print(gson.toJson(jsonObject));
        out.flush();
    }
}