package com.b00tc4mp.data;

import java.util.ArrayList;

public class Data {
    private ArrayList<UserData> users = new ArrayList<>();
    private static Data instance;

    private Data() {}

    public static Data get() {
        if (instance == null) {
            instance = new Data();

            UserData pepito = new UserData("Pepito Grillo", "pepito", "123123123");
            instance.addUser(pepito);
        }
        return instance;
    }

    public void addUser(UserData user) {
        users.add(user);
    }

    public ArrayList<UserData> getUsers() {
        return users;
    }

    public UserData findUserByUsername(String username) {
        for (UserData user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }   

    public void removeUsers() {
        users.clear();
    }

    public UserData findUserById(String id) {
        for (UserData user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }
}
