package com.b00tc4mp.validation;

import com.b00tc4mp.error.ValidationException;

public class Validate {
    public static void name(String name) throws ValidationException {
        if (name == null) {
            throw new ValidationException("name is null");
        }
        if (name.isEmpty()) {
            throw new ValidationException("name is empty");
        }
    }

    public static void username(String username) throws ValidationException {
        if (username == null) {
            throw new ValidationException("username is null");
        }
        if (username.isEmpty()) {
            throw new ValidationException("username is empty");
        }
    }

    public static void password(String password) throws ValidationException {
        if (password == null) {
            throw new ValidationException("password is null");
        }
        if (password.isEmpty()) {
            throw new ValidationException("password is empty");
        }
    }

    public static void passwords(String password, String passwordRepeat) throws ValidationException {
        Validate.password(password);

        if (passwordRepeat == null) {
            throw new ValidationException("passwordRepeat is null");
        }
        if (passwordRepeat.isEmpty()) {
            throw new ValidationException("passwordRepeat is empty");
        }

        if (!password.equals(passwordRepeat)) {
            throw new ValidationException("passwords do not match");
        }
    }

    public static void userId(String userId) throws ValidationException {
        if (userId == null) {
            throw new ValidationException("userId is null");
        }
        if (userId.isEmpty()) {
            throw new ValidationException("userId is empty");
        }
    }
}
