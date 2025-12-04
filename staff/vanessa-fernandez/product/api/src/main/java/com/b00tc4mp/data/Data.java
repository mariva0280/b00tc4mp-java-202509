package com.b00tc4mp.data;

import java.util.List;

public interface Data {

    void addUser(UserData user);

    List<UserData> getUsers();

    UserData findUserByUsername(String username);

    UserData findUserById(String id);

    void removeUsers();
}
