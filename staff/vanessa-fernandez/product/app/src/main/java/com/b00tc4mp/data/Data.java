package com.b00tc4mp.data;

public class Data {

    private String token;

    private static Data instance;

    private Data() {
    }

    public static Data get() {
        return instance == null? instance = new Data() : instance;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
