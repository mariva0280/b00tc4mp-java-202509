package com.b00tc4mp.logic;

public class ZenQuote {
    private String quote;
    private String author;

    public ZenQuote(String quote, String author) {
        this.quote = quote;
        this.author = author;
    }

    public String getQuote() {
        return quote;
    }
    public String getAuthor() {
        return author;
    }
}
