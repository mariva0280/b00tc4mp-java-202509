package com.b00tc4mp.logic;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.b00tc4mp.data.Data;
import com.b00tc4mp.data.UserData;
import com.b00tc4mp.error.DuplicityException;

@Service
public class Logic {
    @Autowired private Data data;

    public void registerUser(String name, String username, String password, String passwordRepeat) throws Exception{
        UserData user = data.findUserByUsername(username);

        if (user != null) {
            throw new DuplicityException("User already exists");
        }

        data.addUser(new UserData(UUID.randomUUID().toString(), name, username, password));
    }
}
